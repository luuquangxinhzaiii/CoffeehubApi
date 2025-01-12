package com.ali.coffeehub.article.service.mapper;

import static com.ali.coffeehub.article.domain.UserArticleInteractionEntityAsserts.*;
import static com.ali.coffeehub.article.domain.UserArticleInteractionEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserArticleInteractionMapperTest {

    private UserArticleInteractionMapper userArticleInteractionMapper;

    @BeforeEach
    void setUp() {
        userArticleInteractionMapper = new UserArticleInteractionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserArticleInteractionEntitySample1();
        var actual = userArticleInteractionMapper.toEntity(userArticleInteractionMapper.toDto(expected));
        assertUserArticleInteractionEntityAllPropertiesEquals(expected, actual);
    }
}
