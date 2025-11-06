package org.pricetrack;


import java.time.Instant;
import java.util.Map;

/*
Class to hold the price data
this will hold all the information releated to the price
 */
public class PriceRecord {

    public PriceRecord(String id, Instant asOfNow, Map<String, Object> payload) {
        this.id = id;
        this.asOfNow = asOfNow;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public Instant getAsOfNow() {
        return asOfNow;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    private final String id;

    private final Instant asOfNow;

    private final Map<String , Object> payload;





}
