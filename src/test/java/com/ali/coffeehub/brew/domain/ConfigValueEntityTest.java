package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.ConfigValueEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigValueEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigValueEntity.class);
        ConfigValueEntity configValueEntity1 = getConfigValueEntitySample1();
        ConfigValueEntity configValueEntity2 = new ConfigValueEntity();
        assertThat(configValueEntity1).isNotEqualTo(configValueEntity2);

        configValueEntity2.setId(configValueEntity1.getId());
        assertThat(configValueEntity1).isEqualTo(configValueEntity2);

        configValueEntity2 = getConfigValueEntitySample2();
        assertThat(configValueEntity1).isNotEqualTo(configValueEntity2);
    }
}
