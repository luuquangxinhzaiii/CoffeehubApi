package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigValueDTO.class);
        ConfigValueDTO configValueDTO1 = new ConfigValueDTO();
        configValueDTO1.setId(1L);
        ConfigValueDTO configValueDTO2 = new ConfigValueDTO();
        assertThat(configValueDTO1).isNotEqualTo(configValueDTO2);
        configValueDTO2.setId(configValueDTO1.getId());
        assertThat(configValueDTO1).isEqualTo(configValueDTO2);
        configValueDTO2.setId(2L);
        assertThat(configValueDTO1).isNotEqualTo(configValueDTO2);
        configValueDTO1.setId(null);
        assertThat(configValueDTO1).isNotEqualTo(configValueDTO2);
    }
}
