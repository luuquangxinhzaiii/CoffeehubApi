package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BrewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrewDTO.class);
        BrewDTO brewDTO1 = new BrewDTO();
        brewDTO1.setId(1L);
        BrewDTO brewDTO2 = new BrewDTO();
        assertThat(brewDTO1).isNotEqualTo(brewDTO2);
        brewDTO2.setId(brewDTO1.getId());
        assertThat(brewDTO1).isEqualTo(brewDTO2);
        brewDTO2.setId(2L);
        assertThat(brewDTO1).isNotEqualTo(brewDTO2);
        brewDTO1.setId(null);
        assertThat(brewDTO1).isNotEqualTo(brewDTO2);
    }
}
