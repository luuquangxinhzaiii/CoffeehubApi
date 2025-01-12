package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserArticleInteractionEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserArticleInteractionEntity getUserArticleInteractionEntitySample1() {
        return new UserArticleInteractionEntity()
            .id(1L)
            .aticleId(1L)
            .userId(1L)
            .readProgress(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static UserArticleInteractionEntity getUserArticleInteractionEntitySample2() {
        return new UserArticleInteractionEntity()
            .id(2L)
            .aticleId(2L)
            .userId(2L)
            .readProgress(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static UserArticleInteractionEntity getUserArticleInteractionEntityRandomSampleGenerator() {
        return new UserArticleInteractionEntity()
            .id(longCount.incrementAndGet())
            .aticleId(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .readProgress(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
