package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.ArticleBodyEntityAsserts.*;
import static com.ali.coffeehub.article.domain.ArticleBodyEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleBodyMapperTest {

    private ArticleBodyMapper articleBodyMapper;

    @BeforeEach
    void setUp() {
        articleBodyMapper = new ArticleBodyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleBodyEntitySample1();
        var actual = articleBodyMapper.toEntity(articleBodyMapper.toDto(expected));
        assertArticleBodyEntityAllPropertiesEquals(expected, actual);
    }
}
