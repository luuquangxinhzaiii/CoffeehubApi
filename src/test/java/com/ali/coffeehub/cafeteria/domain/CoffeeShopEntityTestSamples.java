package com.ali.coffeehub.cafeteria.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CoffeeShopEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CoffeeShopEntity getCoffeeShopEntitySample1() {
        return new CoffeeShopEntity()
            .id(1L)
            .categoryId(1L)
            .name("name1")
            .logoUri("logoUri1")
            .phone("phone1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static CoffeeShopEntity getCoffeeShopEntitySample2() {
        return new CoffeeShopEntity()
            .id(2L)
            .categoryId(2L)
            .name("name2")
            .logoUri("logoUri2")
            .phone("phone2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static CoffeeShopEntity getCoffeeShopEntityRandomSampleGenerator() {
        return new CoffeeShopEntity()
            .id(longCount.incrementAndGet())
            .categoryId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .logoUri(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
