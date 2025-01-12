package com.ali.coffeehub.article.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleReactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleReactionDTO.class);
        ArticleReactionDTO articleReactionDTO1 = new ArticleReactionDTO();
        articleReactionDTO1.setId(1L);
        ArticleReactionDTO articleReactionDTO2 = new ArticleReactionDTO();
        assertThat(articleReactionDTO1).isNotEqualTo(articleReactionDTO2);
        articleReactionDTO2.setId(articleReactionDTO1.getId());
        assertThat(articleReactionDTO1).isEqualTo(articleReactionDTO2);
        articleReactionDTO2.setId(2L);
        assertThat(articleReactionDTO1).isNotEqualTo(articleReactionDTO2);
        articleReactionDTO1.setId(null);
        assertThat(articleReactionDTO1).isNotEqualTo(articleReactionDTO2);
    }
}
