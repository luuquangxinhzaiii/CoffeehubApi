package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.RecipeDetailMediaEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.RecipeDetailMediaEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeDetailMediaMapperTest {

    private RecipeDetailMediaMapper recipeDetailMediaMapper;

    @BeforeEach
    void setUp() {
        recipeDetailMediaMapper = new RecipeDetailMediaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecipeDetailMediaEntitySample1();
        var actual = recipeDetailMediaMapper.toEntity(recipeDetailMediaMapper.toDto(expected));
        assertRecipeDetailMediaEntityAllPropertiesEquals(expected, actual);
    }
}
