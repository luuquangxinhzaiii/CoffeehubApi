package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleReactionEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArticleReactionEntity getArticleReactionEntitySample1() {
        return new ArticleReactionEntity().id(1L).aticleId(1L).userId(1L);
    }

    public static ArticleReactionEntity getArticleReactionEntitySample2() {
        return new ArticleReactionEntity().id(2L).aticleId(2L).userId(2L);
    }

    public static ArticleReactionEntity getArticleReactionEntityRandomSampleGenerator() {
        return new ArticleReactionEntity()
            .id(longCount.incrementAndGet())
            .aticleId(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet());
    }
}
