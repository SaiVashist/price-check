package org.pricetrack;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PriceServiceImplTest {

    private PriceService newService() {
        return new PriceServiceImpl();
    }

    private static PriceRecord rec(String id, String iso, double price) {
        return new PriceRecord(id, Instant.parse(iso), Map.of("price", price));
    }

    @Test
    void staging_is_hidden_until_complete() throws Exception {
        PriceService svc = newService();
        String batch = svc.startBatch();
        svc.uploadChunk(batch, List.of(
                rec("AAPL","2025-11-01T10:00:00Z", 190.0)
        ));
        assertNull(svc.getPriceData("AAPL")); // consumers must not see partial batches
        svc.completeBatch(batch);
        assertEquals(190.0, svc.getPriceData("AAPL").getPayload().get("price"));
    }

    @Test
    void cancel_discards_batch() throws Exception {
        PriceService svc = newService();
        String batch = svc.startBatch();
        svc.uploadChunk(batch, List.of(rec("MSFT","2025-11-01T10:00:00Z", 410.0)));
        svc.cancelBatch(batch);
        assertNull(svc.getPriceData("MSFT"));
    }

    @Test
    void latest_asOf_wins() throws Exception {
        PriceService svc = newService();
        String batch = svc.startBatch();
        svc.uploadChunk(batch, List.of(
                rec("IBM","2025-11-01T10:00:00Z", 100.0),
                rec("IBM","2025-11-01T11:00:00Z", 101.0)
        ));
        svc.completeBatch(batch);
        assertEquals(101.0, svc.getPriceData("IBM").getPayload().get("price"));
    }
}