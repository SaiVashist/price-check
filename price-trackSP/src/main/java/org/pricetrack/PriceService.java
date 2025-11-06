package org.pricetrack;

import java.util.List;

public interface PriceService {


    /*
    This metod will start a new batch
     */
    String startBatch();


    void uploadChunk(String batchID , List<PriceRecord> priceData);

    void completeBatch(String batchId);


    void cancelBatch(String batchId);

    PriceRecord getPriceData(String id);


}
