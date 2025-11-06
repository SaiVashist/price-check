package org.pricetrack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class PriceServiceImpl implements PriceService{


    private final ConcurrentHashMap<String,PriceRecord> lastPrice = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String , BatchContext> batches = new ConcurrentHashMap<>();
    private final UUIDGenerator uuidGenerator = new UUIDGenerator();
    private final ReentrantLock publishLock = new ReentrantLock();
    /**
     * @return
     */
    @Override
    public String startBatch() {

        String batchID;

    }

    /**
     * @param batchID
     * @param priceData
     */
    @Override
    public void uploadChunk(String batchID, List<PriceRecord> priceData) {

    }

    /**
     * @param batchId
     */
    @Override
    public void completeBatch(String batchId) {

    }

    /**
     * @param batchId
     */
    @Override
    public void cancelBatch(String batchId) {

    }

    /**
     * @param id
     * @return
     */
    @Override
    public PriceRecord getPriceData(String id) {
        return null;
    }

    // Helper for unique batch ids
    private static class UUIDGenerator {
        String next() {
            return UUID.randomUUID().toString();
        }
    }
}
