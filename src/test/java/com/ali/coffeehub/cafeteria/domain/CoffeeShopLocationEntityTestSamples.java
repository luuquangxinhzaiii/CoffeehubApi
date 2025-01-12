package com.ali.coffeehub.cafeteria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CoffeeShopLocationEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CoffeeShopLocationEntity getCoffeeShopLocationEntitySample1() {
        return new CoffeeShopLocationEntity().id(1L).coffeeShopId(1L).address("address1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static CoffeeShopLocationEntity getCoffeeShopLocationEntitySample2() {
        return new CoffeeShopLocationEntity().id(2L).coffeeShopId(2L).address("address2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static CoffeeShopLocationEntity getCoffeeShopLocationEntityRandomSampleGenerator() {
        return new CoffeeShopLocationEntity()
            .id(longCount.incrementAndGet())
            .coffeeShopId(longCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
