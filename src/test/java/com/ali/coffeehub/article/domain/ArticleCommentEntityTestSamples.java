package com.ali.coffeehub.article.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleCommentEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArticleCommentEntity getArticleCommentEntitySample1() {
        return new ArticleCommentEntity().id(1L).articleId(1L).userId(1L).parentId(1L);
    }

    public static ArticleCommentEntity getArticleCommentEntitySample2() {
        return new ArticleCommentEntity().id(2L).articleId(2L).userId(2L).parentId(2L);
    }

    public static ArticleCommentEntity getArticleCommentEntityRandomSampleGenerator() {
        return new ArticleCommentEntity()
            .id(longCount.incrementAndGet())
            .articleId(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .parentId(longCount.incrementAndGet());
    }
}
