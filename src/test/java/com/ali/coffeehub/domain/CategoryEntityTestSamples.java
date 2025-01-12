package com.ali.coffeehub.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryEntity getCategoryEntitySample1() {
        return new CategoryEntity()
            .id(1L)
            .name("name1")
            .desciption("desciption1")
            .iconUri("iconUri1")
            .moduleName("moduleName1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static CategoryEntity getCategoryEntitySample2() {
        return new CategoryEntity()
            .id(2L)
            .name("name2")
            .desciption("desciption2")
            .iconUri("iconUri2")
            .moduleName("moduleName2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static CategoryEntity getCategoryEntityRandomSampleGenerator() {
        return new CategoryEntity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .desciption(UUID.randomUUID().toString())
            .iconUri(UUID.randomUUID().toString())
            .moduleName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
