package org.pricetrack;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {


        PriceService svc = new PriceServiceImpl();

        // Start batch
        String batch = svc.startBatch();

        // Upload dummy data
        svc.uploadChunk(batch, List.of(
                new PriceRecord("AAPL", Instant.parse("2025-11-01T10:00:00Z"), Map.of("price", 190.25)),
                new PriceRecord("AAPL", Instant.parse("2025-11-01T11:00:00Z"), Map.of("price", 191.50)),
                new PriceRecord("MSFT", Instant.parse("2025-11-01T09:30:00Z"), Map.of("price", 410.10))
        ));

        System.out.println("Before commit, AAPL visible? " + svc.getPriceData("AAPL"));

        // Commit batch
        svc.completeBatch(batch);

        // After commit
        System.out.println("After commit, AAPL price: " + svc.getPriceData("AAPL"));
        System.out.println("After commit, MSFT price: " + svc.getPriceData("MSFT"));

    }
}