package com.ali.coffeehub.cafeteria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CoffeeShopReactionEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CoffeeShopReactionEntity getCoffeeShopReactionEntitySample1() {
        return new CoffeeShopReactionEntity().id(1L).coffeeShopId(1L).userId(1L).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static CoffeeShopReactionEntity getCoffeeShopReactionEntitySample2() {
        return new CoffeeShopReactionEntity().id(2L).coffeeShopId(2L).userId(2L).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static CoffeeShopReactionEntity getCoffeeShopReactionEntityRandomSampleGenerator() {
        return new CoffeeShopReactionEntity()
            .id(longCount.incrementAndGet())
            .coffeeShopId(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
