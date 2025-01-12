package com.ali.coffeehub.service.mapper;

import static com.ali.coffeehub.domain.CategoryEntityAsserts.*;
import static com.ali.coffeehub.domain.CategoryEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryEntitySample1();
        var actual = categoryMapper.toEntity(categoryMapper.toDto(expected));
        assertCategoryEntityAllPropertiesEquals(expected, actual);
    }
}
