package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.ArticleStatisticEntityAsserts.*;
import static com.ali.coffeehub.article.domain.ArticleStatisticEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleStatisticMapperTest {

    private ArticleStatisticMapper articleStatisticMapper;

    @BeforeEach
    void setUp() {
        articleStatisticMapper = new ArticleStatisticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleStatisticEntitySample1();
        var actual = articleStatisticMapper.toEntity(articleStatisticMapper.toDto(expected));
        assertArticleStatisticEntityAllPropertiesEquals(expected, actual);
    }
}
