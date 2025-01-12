package com.ali.coffeehub.roaster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoasterEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoasterEntity getRoasterEntitySample1() {
        return new RoasterEntity()
            .id(1L)
            .categoryId(1L)
            .name("name1")
            .description("description1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static RoasterEntity getRoasterEntitySample2() {
        return new RoasterEntity()
            .id(2L)
            .categoryId(2L)
            .name("name2")
            .description("description2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static RoasterEntity getRoasterEntityRandomSampleGenerator() {
        return new RoasterEntity()
            .id(longCount.incrementAndGet())
            .categoryId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
