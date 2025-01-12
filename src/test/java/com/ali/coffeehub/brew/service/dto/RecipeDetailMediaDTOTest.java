package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeDetailMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeDetailMediaDTO.class);
        RecipeDetailMediaDTO recipeDetailMediaDTO1 = new RecipeDetailMediaDTO();
        recipeDetailMediaDTO1.setId(1L);
        RecipeDetailMediaDTO recipeDetailMediaDTO2 = new RecipeDetailMediaDTO();
        assertThat(recipeDetailMediaDTO1).isNotEqualTo(recipeDetailMediaDTO2);
        recipeDetailMediaDTO2.setId(recipeDetailMediaDTO1.getId());
        assertThat(recipeDetailMediaDTO1).isEqualTo(recipeDetailMediaDTO2);
        recipeDetailMediaDTO2.setId(2L);
        assertThat(recipeDetailMediaDTO1).isNotEqualTo(recipeDetailMediaDTO2);
        recipeDetailMediaDTO1.setId(null);
        assertThat(recipeDetailMediaDTO1).isNotEqualTo(recipeDetailMediaDTO2);
    }
}
