package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.StepEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StepEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StepEntity.class);
        StepEntity stepEntity1 = getStepEntitySample1();
        StepEntity stepEntity2 = new StepEntity();
        assertThat(stepEntity1).isNotEqualTo(stepEntity2);

        stepEntity2.setId(stepEntity1.getId());
        assertThat(stepEntity1).isEqualTo(stepEntity2);

        stepEntity2 = getStepEntitySample2();
        assertThat(stepEntity1).isNotEqualTo(stepEntity2);
    }
}
