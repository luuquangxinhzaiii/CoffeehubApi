package com.ali.coffeehub.article.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleStatisticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleStatisticDTO.class);
        ArticleStatisticDTO articleStatisticDTO1 = new ArticleStatisticDTO();
        articleStatisticDTO1.setId(1L);
        ArticleStatisticDTO articleStatisticDTO2 = new ArticleStatisticDTO();
        assertThat(articleStatisticDTO1).isNotEqualTo(articleStatisticDTO2);
        articleStatisticDTO2.setId(articleStatisticDTO1.getId());
        assertThat(articleStatisticDTO1).isEqualTo(articleStatisticDTO2);
        articleStatisticDTO2.setId(2L);
        assertThat(articleStatisticDTO1).isNotEqualTo(articleStatisticDTO2);
        articleStatisticDTO1.setId(null);
        assertThat(articleStatisticDTO1).isNotEqualTo(articleStatisticDTO2);
    }
}
