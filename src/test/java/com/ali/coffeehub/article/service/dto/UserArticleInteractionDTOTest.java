package com.ali.coffeehub.article.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserArticleInteractionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserArticleInteractionDTO.class);
        UserArticleInteractionDTO userArticleInteractionDTO1 = new UserArticleInteractionDTO();
        userArticleInteractionDTO1.setId(1L);
        UserArticleInteractionDTO userArticleInteractionDTO2 = new UserArticleInteractionDTO();
        assertThat(userArticleInteractionDTO1).isNotEqualTo(userArticleInteractionDTO2);
        userArticleInteractionDTO2.setId(userArticleInteractionDTO1.getId());
        assertThat(userArticleInteractionDTO1).isEqualTo(userArticleInteractionDTO2);
        userArticleInteractionDTO2.setId(2L);
        assertThat(userArticleInteractionDTO1).isNotEqualTo(userArticleInteractionDTO2);
        userArticleInteractionDTO1.setId(null);
        assertThat(userArticleInteractionDTO1).isNotEqualTo(userArticleInteractionDTO2);
    }
}
