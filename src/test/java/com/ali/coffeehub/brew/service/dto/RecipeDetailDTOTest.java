package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeDetailDTO.class);
        RecipeDetailDTO recipeDetailDTO1 = new RecipeDetailDTO();
        recipeDetailDTO1.setId(1L);
        RecipeDetailDTO recipeDetailDTO2 = new RecipeDetailDTO();
        assertThat(recipeDetailDTO1).isNotEqualTo(recipeDetailDTO2);
        recipeDetailDTO2.setId(recipeDetailDTO1.getId());
        assertThat(recipeDetailDTO1).isEqualTo(recipeDetailDTO2);
        recipeDetailDTO2.setId(2L);
        assertThat(recipeDetailDTO1).isNotEqualTo(recipeDetailDTO2);
        recipeDetailDTO1.setId(null);
        assertThat(recipeDetailDTO1).isNotEqualTo(recipeDetailDTO2);
    }
}
