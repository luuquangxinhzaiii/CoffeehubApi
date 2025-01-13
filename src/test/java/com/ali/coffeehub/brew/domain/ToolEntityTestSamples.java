package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ToolEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ToolEntity getToolEntitySample1() {
        return new ToolEntity().id(1L).brewId(1L).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static ToolEntity getToolEntitySample2() {
        return new ToolEntity().id(2L).brewId(2L).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static ToolEntity getToolEntityRandomSampleGenerator() {
        return new ToolEntity()
            .id(longCount.incrementAndGet())
            .brewId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
