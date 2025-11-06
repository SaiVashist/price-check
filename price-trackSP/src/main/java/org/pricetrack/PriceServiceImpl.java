package org.pricetrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class PriceServiceImpl implements PriceService{

    /**
     * In-memory implementation of PriceService.
     * Thread-safe and resilient to incorrect producer/consumer usage.
     */

    // Holds the last published price for each instrument id

    private final ConcurrentHashMap<String,PriceRecord> lastPrices = new ConcurrentHashMap<>();
    // Holds batches in progress

    private final ConcurrentHashMap<String , BatchContext> batches = new ConcurrentHashMap<>();
    // Used to generate unique batch ids
    private final UUIDGenerator uuidGenerator = new UUIDGenerator();
    // Lock for atomic batch completion
    private final ReentrantLock publishLock = new ReentrantLock();
    /**
     * @return
     */
    @Override
    public String startBatch() throws Exception {

        String batchID = uuidGenerator.next();
        BatchContext batch = new BatchContext(batchID);
        if(batches.putIfAbsent(batchID,batch) != null){
            throw new Exception("Batch already exits");
        }

        return batchID;

    }

    /**
     * @param batchID
     * @param priceData
     *
     * In this method we will add batch data to our storage
     */
    @Override
    public void uploadChunk(String batchID, List<PriceRecord> priceData) throws Exception {

        BatchContext batch = batches.get(batchID);
        if(batch == null || batch.isFinalized()){
            throw new Exception("Batch is not active");
        }
        // do null check for records befor storting
        if(priceData == null && !(priceData.size() > 0)){
            throw new Exception("Price data should not be empty");

        }

        batch.addRecords(priceData);

    }

    /**
     * @param batchId
     */
    @Override
    public void completeBatch(String batchId) {

        BatchContext batch = batches.get(batchId);
        if (batch == null || batch.isFinalized()) {
            throw new IllegalStateException("Batch not active: " + batchId);
        }
        publishLock.lock();
        try {
            // Atomically publish all records
            for (PriceRecord record : batch.getAllRecords()) {
                lastPrices.merge(record.getId(), record, (oldRec, newRec) ->
                        newRec.getAsOfNow().isAfter(oldRec.getAsOfNow()) ? newRec : oldRec);
            }
            batch.finalizeBatch();
        } finally {
            publishLock.unlock();
        }
        batches.remove(batchId);

    }

    /**
     * @param batchId
     */
    @Override
    public void cancelBatch(String batchId) {
        BatchContext batch = batches.get(batchId);
        if (batch == null || batch.isFinalized()) {
            throw new IllegalStateException("Batch not active: " + batchId);
        }
        batch.finalizeBatch();
        batches.remove(batchId);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PriceRecord getPriceData(String id) {
        return lastPrices.get(id);
    }

    // Helper for unique batch ids
    private static class UUIDGenerator {
        String next() {
            return UUID.randomUUID().toString();
        }
    }


    // Helper class for batch context
    private static class BatchContext {
        private final String batchId;
        private final List<PriceRecord> records = new ArrayList<>();
        private final AtomicBoolean finalized = new AtomicBoolean(false);

        BatchContext(String batchId) {
            this.batchId = batchId;
        }

        void addRecords(List<PriceRecord> chunk) {
            if (finalized.get()) throw new IllegalStateException("Batch finalized");
            records.addAll(chunk);
        }

        List<PriceRecord> getAllRecords() {
            return Collections.unmodifiableList(records);
        }

        void finalizeBatch() {
            finalized.set(true);
        }

        boolean isFinalized() {
            return finalized.get();
        }
    }
}

