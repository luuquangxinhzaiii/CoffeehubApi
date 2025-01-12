package com.ali.coffeehub.roaster.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoasterDTO.class);
        RoasterDTO roasterDTO1 = new RoasterDTO();
        roasterDTO1.setId(1L);
        RoasterDTO roasterDTO2 = new RoasterDTO();
        assertThat(roasterDTO1).isNotEqualTo(roasterDTO2);
        roasterDTO2.setId(roasterDTO1.getId());
        assertThat(roasterDTO1).isEqualTo(roasterDTO2);
        roasterDTO2.setId(2L);
        assertThat(roasterDTO1).isNotEqualTo(roasterDTO2);
        roasterDTO1.setId(null);
        assertThat(roasterDTO1).isNotEqualTo(roasterDTO2);
    }
}
