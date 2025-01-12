package com.ali.coffeehub.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemConfigEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemConfigEntity getSystemConfigEntitySample1() {
        return new SystemConfigEntity()
            .id(1L)
            .key("key1")
            .value("value1")
            .description("description1")
            .moduleName("moduleName1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static SystemConfigEntity getSystemConfigEntitySample2() {
        return new SystemConfigEntity()
            .id(2L)
            .key("key2")
            .value("value2")
            .description("description2")
            .moduleName("moduleName2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static SystemConfigEntity getSystemConfigEntityRandomSampleGenerator() {
        return new SystemConfigEntity()
            .id(longCount.incrementAndGet())
            .key(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .moduleName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
