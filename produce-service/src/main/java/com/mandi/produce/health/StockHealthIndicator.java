package com.mandi.produce.health;

import com.mandi.produce.ProduceService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Reports DOWN when the total stock of all vegetables is below 1000 kg.
 */
@Component
public class StockHealthIndicator implements HealthIndicator {

    private static final int MIN_TOTAL_STOCK_KG = 1000;

    private final ProduceService produceService;

    public StockHealthIndicator(ProduceService produceService) {
        this.produceService = produceService;
    }

    @Override
    public Health health() {
        int total = produceService.getTotalStockKg();
        if (total < MIN_TOTAL_STOCK_KG) {
            return Health.down().withDetail("totalStockKg", total).build();
        }
        return Health.up().withDetail("totalStockKg", total).build();
    }
}
