package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.ArticleCommentEntityAsserts.*;
import static com.ali.coffeehub.article.domain.ArticleCommentEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleCommentMapperTest {

    private ArticleCommentMapper articleCommentMapper;

    @BeforeEach
    void setUp() {
        articleCommentMapper = new ArticleCommentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleCommentEntitySample1();
        var actual = articleCommentMapper.toEntity(articleCommentMapper.toDto(expected));
        assertArticleCommentEntityAllPropertiesEquals(expected, actual);
    }
}
