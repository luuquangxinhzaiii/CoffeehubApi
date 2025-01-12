package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TimelineEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TimelineEntity getTimelineEntitySample1() {
        return new TimelineEntity()
            .id(1L)
            .configId(1L)
            .configValueId(1L)
            .recipeId(1L)
            .startTime(1)
            .ratio(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static TimelineEntity getTimelineEntitySample2() {
        return new TimelineEntity()
            .id(2L)
            .configId(2L)
            .configValueId(2L)
            .recipeId(2L)
            .startTime(2)
            .ratio(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static TimelineEntity getTimelineEntityRandomSampleGenerator() {
        return new TimelineEntity()
            .id(longCount.incrementAndGet())
            .configId(longCount.incrementAndGet())
            .configValueId(longCount.incrementAndGet())
            .recipeId(longCount.incrementAndGet())
            .startTime(intCount.incrementAndGet())
            .ratio(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
