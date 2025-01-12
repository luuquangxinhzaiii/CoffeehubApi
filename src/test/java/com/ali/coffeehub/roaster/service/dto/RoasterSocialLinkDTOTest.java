package com.ali.coffeehub.roaster.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoasterSocialLinkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoasterSocialLinkDTO.class);
        RoasterSocialLinkDTO roasterSocialLinkDTO1 = new RoasterSocialLinkDTO();
        roasterSocialLinkDTO1.setId(1L);
        RoasterSocialLinkDTO roasterSocialLinkDTO2 = new RoasterSocialLinkDTO();
        assertThat(roasterSocialLinkDTO1).isNotEqualTo(roasterSocialLinkDTO2);
        roasterSocialLinkDTO2.setId(roasterSocialLinkDTO1.getId());
        assertThat(roasterSocialLinkDTO1).isEqualTo(roasterSocialLinkDTO2);
        roasterSocialLinkDTO2.setId(2L);
        assertThat(roasterSocialLinkDTO1).isNotEqualTo(roasterSocialLinkDTO2);
        roasterSocialLinkDTO1.setId(null);
        assertThat(roasterSocialLinkDTO1).isNotEqualTo(roasterSocialLinkDTO2);
    }
}
