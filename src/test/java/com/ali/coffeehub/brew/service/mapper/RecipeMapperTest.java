package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.RecipeEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.RecipeEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    void setUp() {
        recipeMapper = new RecipeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecipeEntitySample1();
        var actual = recipeMapper.toEntity(recipeMapper.toDto(expected));
        assertRecipeEntityAllPropertiesEquals(expected, actual);
    }
}
