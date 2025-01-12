package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfigEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConfigEntity getConfigEntitySample1() {
        return new ConfigEntity().id(1L).recipeId(1L).name("name1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static ConfigEntity getConfigEntitySample2() {
        return new ConfigEntity().id(2L).recipeId(2L).name("name2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static ConfigEntity getConfigEntityRandomSampleGenerator() {
        return new ConfigEntity()
            .id(longCount.incrementAndGet())
            .recipeId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
