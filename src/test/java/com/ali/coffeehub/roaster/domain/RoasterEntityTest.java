package com.ali.coffeehub.roaster.domain;

import static com.ali.coffeehub.roaster.domain.RoasterEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoasterEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoasterEntity.class);
        RoasterEntity roasterEntity1 = getRoasterEntitySample1();
        RoasterEntity roasterEntity2 = new RoasterEntity();
        assertThat(roasterEntity1).isNotEqualTo(roasterEntity2);

        roasterEntity2.setId(roasterEntity1.getId());
        assertThat(roasterEntity1).isEqualTo(roasterEntity2);

        roasterEntity2 = getRoasterEntitySample2();
        assertThat(roasterEntity1).isNotEqualTo(roasterEntity2);
    }
}
