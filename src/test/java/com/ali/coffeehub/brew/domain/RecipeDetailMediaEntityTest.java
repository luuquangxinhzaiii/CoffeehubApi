package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.RecipeDetailMediaEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeDetailMediaEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeDetailMediaEntity.class);
        RecipeDetailMediaEntity recipeDetailMediaEntity1 = getRecipeDetailMediaEntitySample1();
        RecipeDetailMediaEntity recipeDetailMediaEntity2 = new RecipeDetailMediaEntity();
        assertThat(recipeDetailMediaEntity1).isNotEqualTo(recipeDetailMediaEntity2);

        recipeDetailMediaEntity2.setId(recipeDetailMediaEntity1.getId());
        assertThat(recipeDetailMediaEntity1).isEqualTo(recipeDetailMediaEntity2);

        recipeDetailMediaEntity2 = getRecipeDetailMediaEntitySample2();
        assertThat(recipeDetailMediaEntity1).isNotEqualTo(recipeDetailMediaEntity2);
    }
}
