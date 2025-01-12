package com.ali.coffeehub.roaster.domain;

import static com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoasterSocialLinkEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoasterSocialLinkEntity.class);
        RoasterSocialLinkEntity roasterSocialLinkEntity1 = getRoasterSocialLinkEntitySample1();
        RoasterSocialLinkEntity roasterSocialLinkEntity2 = new RoasterSocialLinkEntity();
        assertThat(roasterSocialLinkEntity1).isNotEqualTo(roasterSocialLinkEntity2);

        roasterSocialLinkEntity2.setId(roasterSocialLinkEntity1.getId());
        assertThat(roasterSocialLinkEntity1).isEqualTo(roasterSocialLinkEntity2);

        roasterSocialLinkEntity2 = getRoasterSocialLinkEntitySample2();
        assertThat(roasterSocialLinkEntity1).isNotEqualTo(roasterSocialLinkEntity2);
    }
}
