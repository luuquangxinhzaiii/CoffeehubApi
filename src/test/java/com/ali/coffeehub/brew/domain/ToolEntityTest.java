package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.ToolEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToolEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolEntity.class);
        ToolEntity toolEntity1 = getToolEntitySample1();
        ToolEntity toolEntity2 = new ToolEntity();
        assertThat(toolEntity1).isNotEqualTo(toolEntity2);

        toolEntity2.setId(toolEntity1.getId());
        assertThat(toolEntity1).isEqualTo(toolEntity2);

        toolEntity2 = getToolEntitySample2();
        assertThat(toolEntity1).isNotEqualTo(toolEntity2);
    }
}
