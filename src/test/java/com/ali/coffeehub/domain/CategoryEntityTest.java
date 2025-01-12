package com.ali.coffeehub.domain;

import static com.ali.coffeehub.domain.CategoryEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoryEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryEntity.class);
        CategoryEntity categoryEntity1 = getCategoryEntitySample1();
        CategoryEntity categoryEntity2 = new CategoryEntity();
        assertThat(categoryEntity1).isNotEqualTo(categoryEntity2);

        categoryEntity2.setId(categoryEntity1.getId());
        assertThat(categoryEntity1).isEqualTo(categoryEntity2);

        categoryEntity2 = getCategoryEntitySample2();
        assertThat(categoryEntity1).isNotEqualTo(categoryEntity2);
    }
}
