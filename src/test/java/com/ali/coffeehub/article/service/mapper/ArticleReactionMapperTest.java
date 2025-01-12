package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.ArticleReactionEntityAsserts.*;
import static com.ali.coffeehub.article.domain.ArticleReactionEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleReactionMapperTest {

    private ArticleReactionMapper articleReactionMapper;

    @BeforeEach
    void setUp() {
        articleReactionMapper = new ArticleReactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleReactionEntitySample1();
        var actual = articleReactionMapper.toEntity(articleReactionMapper.toDto(expected));
        assertArticleReactionEntityAllPropertiesEquals(expected, actual);
    }
}
