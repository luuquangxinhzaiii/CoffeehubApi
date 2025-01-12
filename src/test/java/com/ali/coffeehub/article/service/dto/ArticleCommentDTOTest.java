package com.ali.coffeehub.article.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleCommentDTO.class);
        ArticleCommentDTO articleCommentDTO1 = new ArticleCommentDTO();
        articleCommentDTO1.setId(1L);
        ArticleCommentDTO articleCommentDTO2 = new ArticleCommentDTO();
        assertThat(articleCommentDTO1).isNotEqualTo(articleCommentDTO2);
        articleCommentDTO2.setId(articleCommentDTO1.getId());
        assertThat(articleCommentDTO1).isEqualTo(articleCommentDTO2);
        articleCommentDTO2.setId(2L);
        assertThat(articleCommentDTO1).isNotEqualTo(articleCommentDTO2);
        articleCommentDTO1.setId(null);
        assertThat(articleCommentDTO1).isNotEqualTo(articleCommentDTO2);
    }
}
