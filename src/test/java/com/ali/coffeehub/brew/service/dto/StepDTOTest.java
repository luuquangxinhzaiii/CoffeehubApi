package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StepDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StepDTO.class);
        StepDTO stepDTO1 = new StepDTO();
        stepDTO1.setId(1L);
        StepDTO stepDTO2 = new StepDTO();
        assertThat(stepDTO1).isNotEqualTo(stepDTO2);
        stepDTO2.setId(stepDTO1.getId());
        assertThat(stepDTO1).isEqualTo(stepDTO2);
        stepDTO2.setId(2L);
        assertThat(stepDTO1).isNotEqualTo(stepDTO2);
        stepDTO1.setId(null);
        assertThat(stepDTO1).isNotEqualTo(stepDTO2);
    }
}
