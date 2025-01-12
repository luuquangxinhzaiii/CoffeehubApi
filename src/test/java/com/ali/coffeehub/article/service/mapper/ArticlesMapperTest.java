package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.ArticlesEntityAsserts.*;
import static com.ali.coffeehub.article.domain.ArticlesEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticlesMapperTest {

    private ArticlesMapper articlesMapper;

    @BeforeEach
    void setUp() {
        articlesMapper = new ArticlesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticlesEntitySample1();
        var actual = articlesMapper.toEntity(articlesMapper.toDto(expected));
        assertArticlesEntityAllPropertiesEquals(expected, actual);
    }
}
