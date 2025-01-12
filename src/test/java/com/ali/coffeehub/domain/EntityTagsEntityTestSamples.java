package com.ali.coffeehub.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntityTagsEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EntityTagsEntity getEntityTagsEntitySample1() {
        return new EntityTagsEntity().id(1L).tagId(1L).entityType(1L).entityId(1L).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static EntityTagsEntity getEntityTagsEntitySample2() {
        return new EntityTagsEntity().id(2L).tagId(2L).entityType(2L).entityId(2L).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static EntityTagsEntity getEntityTagsEntityRandomSampleGenerator() {
        return new EntityTagsEntity()
            .id(longCount.incrementAndGet())
            .tagId(longCount.incrementAndGet())
            .entityType(longCount.incrementAndGet())
            .entityId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
