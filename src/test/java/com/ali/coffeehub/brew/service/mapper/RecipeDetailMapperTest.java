package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.RecipeDetailEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.RecipeDetailEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeDetailMapperTest {

    private RecipeDetailMapper recipeDetailMapper;

    @BeforeEach
    void setUp() {
        recipeDetailMapper = new RecipeDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecipeDetailEntitySample1();
        var actual = recipeDetailMapper.toEntity(recipeDetailMapper.toDto(expected));
        assertRecipeDetailEntityAllPropertiesEquals(expected, actual);
    }
}
