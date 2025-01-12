package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BrewEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BrewEntity getBrewEntitySample1() {
        return new BrewEntity()
            .id(1L)
            .categoryId(1L)
            .name("name1")
            .description("description1")
            .level(1)
            .serving("serving1")
            .iconUri("iconUri1")
            .imageUri("imageUri1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static BrewEntity getBrewEntitySample2() {
        return new BrewEntity()
            .id(2L)
            .categoryId(2L)
            .name("name2")
            .description("description2")
            .level(2)
            .serving("serving2")
            .iconUri("iconUri2")
            .imageUri("imageUri2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static BrewEntity getBrewEntityRandomSampleGenerator() {
        return new BrewEntity()
            .id(longCount.incrementAndGet())
            .categoryId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .level(intCount.incrementAndGet())
            .serving(UUID.randomUUID().toString())
            .iconUri(UUID.randomUUID().toString())
            .imageUri(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
