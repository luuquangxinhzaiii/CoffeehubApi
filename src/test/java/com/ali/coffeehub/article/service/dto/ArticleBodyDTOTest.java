package com.ali.coffeehub.article.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleBodyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleBodyDTO.class);
        ArticleBodyDTO articleBodyDTO1 = new ArticleBodyDTO();
        articleBodyDTO1.setId(1L);
        ArticleBodyDTO articleBodyDTO2 = new ArticleBodyDTO();
        assertThat(articleBodyDTO1).isNotEqualTo(articleBodyDTO2);
        articleBodyDTO2.setId(articleBodyDTO1.getId());
        assertThat(articleBodyDTO1).isEqualTo(articleBodyDTO2);
        articleBodyDTO2.setId(2L);
        assertThat(articleBodyDTO1).isNotEqualTo(articleBodyDTO2);
        articleBodyDTO1.setId(null);
        assertThat(articleBodyDTO1).isNotEqualTo(articleBodyDTO2);
    }
}
