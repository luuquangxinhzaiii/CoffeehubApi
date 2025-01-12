package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfigValueEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConfigValueEntity getConfigValueEntitySample1() {
        return new ConfigValueEntity().id(1L).configId(1L).value("value1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static ConfigValueEntity getConfigValueEntitySample2() {
        return new ConfigValueEntity().id(2L).configId(2L).value("value2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static ConfigValueEntity getConfigValueEntityRandomSampleGenerator() {
        return new ConfigValueEntity()
            .id(longCount.incrementAndGet())
            .configId(longCount.incrementAndGet())
            .value(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
