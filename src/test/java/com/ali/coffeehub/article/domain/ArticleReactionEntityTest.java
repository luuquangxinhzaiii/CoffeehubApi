package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.ArticleReactionEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleReactionEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleReactionEntity.class);
        ArticleReactionEntity articleReactionEntity1 = getArticleReactionEntitySample1();
        ArticleReactionEntity articleReactionEntity2 = new ArticleReactionEntity();
        assertThat(articleReactionEntity1).isNotEqualTo(articleReactionEntity2);

        articleReactionEntity2.setId(articleReactionEntity1.getId());
        assertThat(articleReactionEntity1).isEqualTo(articleReactionEntity2);

        articleReactionEntity2 = getArticleReactionEntitySample2();
        assertThat(articleReactionEntity1).isNotEqualTo(articleReactionEntity2);
    }
}
