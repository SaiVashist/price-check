package org.pricetrack;

import java.util.List;

public interface PriceService {


    /*
    This metod will start a new batch
     */
    String startBatch() throws Exception;


    void uploadChunk(String batchID , List<PriceRecord> priceData) throws Exception;

    void completeBatch(String batchId);


    void cancelBatch(String batchId);

    PriceRecord getPriceData(String id);


}
