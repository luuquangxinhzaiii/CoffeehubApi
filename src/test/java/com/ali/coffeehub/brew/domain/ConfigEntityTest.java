package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.ConfigEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigEntity.class);
        ConfigEntity configEntity1 = getConfigEntitySample1();
        ConfigEntity configEntity2 = new ConfigEntity();
        assertThat(configEntity1).isNotEqualTo(configEntity2);

        configEntity2.setId(configEntity1.getId());
        assertThat(configEntity1).isEqualTo(configEntity2);

        configEntity2 = getConfigEntitySample2();
        assertThat(configEntity1).isNotEqualTo(configEntity2);
    }
}
