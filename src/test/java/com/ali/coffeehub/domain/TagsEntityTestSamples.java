package com.ali.coffeehub.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TagsEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TagsEntity getTagsEntitySample1() {
        return new TagsEntity()
            .id(1L)
            .name("name1")
            .slug("slug1")
            .description("description1")
            .iconUrl("iconUrl1")
            .parentId(1L)
            .displayOrder(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static TagsEntity getTagsEntitySample2() {
        return new TagsEntity()
            .id(2L)
            .name("name2")
            .slug("slug2")
            .description("description2")
            .iconUrl("iconUrl2")
            .parentId(2L)
            .displayOrder(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static TagsEntity getTagsEntityRandomSampleGenerator() {
        return new TagsEntity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .iconUrl(UUID.randomUUID().toString())
            .parentId(longCount.incrementAndGet())
            .displayOrder(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
