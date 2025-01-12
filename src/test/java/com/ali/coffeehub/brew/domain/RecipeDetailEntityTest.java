package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.RecipeDetailEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeDetailEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeDetailEntity.class);
        RecipeDetailEntity recipeDetailEntity1 = getRecipeDetailEntitySample1();
        RecipeDetailEntity recipeDetailEntity2 = new RecipeDetailEntity();
        assertThat(recipeDetailEntity1).isNotEqualTo(recipeDetailEntity2);

        recipeDetailEntity2.setId(recipeDetailEntity1.getId());
        assertThat(recipeDetailEntity1).isEqualTo(recipeDetailEntity2);

        recipeDetailEntity2 = getRecipeDetailEntitySample2();
        assertThat(recipeDetailEntity1).isNotEqualTo(recipeDetailEntity2);
    }
}
