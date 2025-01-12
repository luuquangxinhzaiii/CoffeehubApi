package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleBodyEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArticleBodyEntity getArticleBodyEntitySample1() {
        return new ArticleBodyEntity().id(1L);
    }

    public static ArticleBodyEntity getArticleBodyEntitySample2() {
        return new ArticleBodyEntity().id(2L);
    }

    public static ArticleBodyEntity getArticleBodyEntityRandomSampleGenerator() {
        return new ArticleBodyEntity().id(longCount.incrementAndGet());
    }
}
