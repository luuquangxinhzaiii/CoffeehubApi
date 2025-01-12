package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.ArticleCommentEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleCommentEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleCommentEntity.class);
        ArticleCommentEntity articleCommentEntity1 = getArticleCommentEntitySample1();
        ArticleCommentEntity articleCommentEntity2 = new ArticleCommentEntity();
        assertThat(articleCommentEntity1).isNotEqualTo(articleCommentEntity2);

        articleCommentEntity2.setId(articleCommentEntity1.getId());
        assertThat(articleCommentEntity1).isEqualTo(articleCommentEntity2);

        articleCommentEntity2 = getArticleCommentEntitySample2();
        assertThat(articleCommentEntity1).isNotEqualTo(articleCommentEntity2);
    }
}
