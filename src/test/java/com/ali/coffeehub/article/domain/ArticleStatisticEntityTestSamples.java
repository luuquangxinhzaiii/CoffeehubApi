package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleStatisticEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ArticleStatisticEntity getArticleStatisticEntitySample1() {
        return new ArticleStatisticEntity().id(1L).aticleId(1L).viewCount(1).likeCount(1).commentCount(1).avgTimeSpent(1);
    }

    public static ArticleStatisticEntity getArticleStatisticEntitySample2() {
        return new ArticleStatisticEntity().id(2L).aticleId(2L).viewCount(2).likeCount(2).commentCount(2).avgTimeSpent(2);
    }

    public static ArticleStatisticEntity getArticleStatisticEntityRandomSampleGenerator() {
        return new ArticleStatisticEntity()
            .id(longCount.incrementAndGet())
            .aticleId(longCount.incrementAndGet())
            .viewCount(intCount.incrementAndGet())
            .likeCount(intCount.incrementAndGet())
            .commentCount(intCount.incrementAndGet())
            .avgTimeSpent(intCount.incrementAndGet());
    }
}
