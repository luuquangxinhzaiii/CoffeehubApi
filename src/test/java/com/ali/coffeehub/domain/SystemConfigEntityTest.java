package com.ali.coffeehub.domain;

import static com.ali.coffeehub.domain.SystemConfigEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemConfigEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfigEntity.class);
        SystemConfigEntity systemConfigEntity1 = getSystemConfigEntitySample1();
        SystemConfigEntity systemConfigEntity2 = new SystemConfigEntity();
        assertThat(systemConfigEntity1).isNotEqualTo(systemConfigEntity2);

        systemConfigEntity2.setId(systemConfigEntity1.getId());
        assertThat(systemConfigEntity1).isEqualTo(systemConfigEntity2);

        systemConfigEntity2 = getSystemConfigEntitySample2();
        assertThat(systemConfigEntity1).isNotEqualTo(systemConfigEntity2);
    }
}
