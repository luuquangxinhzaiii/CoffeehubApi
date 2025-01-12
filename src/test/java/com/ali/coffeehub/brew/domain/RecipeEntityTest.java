package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.RecipeEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeEntity.class);
        RecipeEntity recipeEntity1 = getRecipeEntitySample1();
        RecipeEntity recipeEntity2 = new RecipeEntity();
        assertThat(recipeEntity1).isNotEqualTo(recipeEntity2);

        recipeEntity2.setId(recipeEntity1.getId());
        assertThat(recipeEntity1).isEqualTo(recipeEntity2);

        recipeEntity2 = getRecipeEntitySample2();
        assertThat(recipeEntity1).isNotEqualTo(recipeEntity2);
    }
}
