package com.ali.coffeehub.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MediaEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MediaEntity getMediaEntitySample1() {
        return new MediaEntity()
            .id(1L)
            .name("name1")
            .entityType("entityType1")
            .entityId(1L)
            .fileName("fileName1")
            .fileType("fileType1")
            .mimeType("mimeType1")
            .fileSize(1L)
            .fileUri("fileUri1")
            .thumbnailUri("thumbnailUri1")
            .width(1)
            .height(1)
            .duration(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static MediaEntity getMediaEntitySample2() {
        return new MediaEntity()
            .id(2L)
            .name("name2")
            .entityType("entityType2")
            .entityId(2L)
            .fileName("fileName2")
            .fileType("fileType2")
            .mimeType("mimeType2")
            .fileSize(2L)
            .fileUri("fileUri2")
            .thumbnailUri("thumbnailUri2")
            .width(2)
            .height(2)
            .duration(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static MediaEntity getMediaEntityRandomSampleGenerator() {
        return new MediaEntity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .entityId(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .fileType(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .fileUri(UUID.randomUUID().toString())
            .thumbnailUri(UUID.randomUUID().toString())
            .width(intCount.incrementAndGet())
            .height(intCount.incrementAndGet())
            .duration(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
