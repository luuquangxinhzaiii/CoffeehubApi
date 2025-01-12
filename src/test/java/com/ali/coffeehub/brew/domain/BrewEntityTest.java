package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.BrewEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BrewEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrewEntity.class);
        BrewEntity brewEntity1 = getBrewEntitySample1();
        BrewEntity brewEntity2 = new BrewEntity();
        assertThat(brewEntity1).isNotEqualTo(brewEntity2);

        brewEntity2.setId(brewEntity1.getId());
        assertThat(brewEntity1).isEqualTo(brewEntity2);

        brewEntity2 = getBrewEntitySample2();
        assertThat(brewEntity1).isNotEqualTo(brewEntity2);
    }
}
