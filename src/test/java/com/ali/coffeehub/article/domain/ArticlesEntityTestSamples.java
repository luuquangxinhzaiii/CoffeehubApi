package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArticlesEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ArticlesEntity getArticlesEntitySample1() {
        return new ArticlesEntity()
            .id(1L)
            .categoryID(1L)
            .bodyId(1L)
            .authorId(1L)
            .title("title1")
            .slug("slug1")
            .subTitle("subTitle1")
            .thumbnailUrl("thumbnailUrl1")
            .readingTime(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static ArticlesEntity getArticlesEntitySample2() {
        return new ArticlesEntity()
            .id(2L)
            .categoryID(2L)
            .bodyId(2L)
            .authorId(2L)
            .title("title2")
            .slug("slug2")
            .subTitle("subTitle2")
            .thumbnailUrl("thumbnailUrl2")
            .readingTime(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static ArticlesEntity getArticlesEntityRandomSampleGenerator() {
        return new ArticlesEntity()
            .id(longCount.incrementAndGet())
            .categoryID(longCount.incrementAndGet())
            .bodyId(longCount.incrementAndGet())
            .authorId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .subTitle(UUID.randomUUID().toString())
            .thumbnailUrl(UUID.randomUUID().toString())
            .readingTime(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
