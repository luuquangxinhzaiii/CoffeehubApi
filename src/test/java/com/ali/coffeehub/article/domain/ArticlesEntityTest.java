package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.ArticlesEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticlesEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticlesEntity.class);
        ArticlesEntity articlesEntity1 = getArticlesEntitySample1();
        ArticlesEntity articlesEntity2 = new ArticlesEntity();
        assertThat(articlesEntity1).isNotEqualTo(articlesEntity2);

        articlesEntity2.setId(articlesEntity1.getId());
        assertThat(articlesEntity1).isEqualTo(articlesEntity2);

        articlesEntity2 = getArticlesEntitySample2();
        assertThat(articlesEntity1).isNotEqualTo(articlesEntity2);
    }
}
