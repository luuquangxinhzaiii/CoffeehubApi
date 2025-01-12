package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.ArticleStatisticEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleStatisticEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleStatisticEntity.class);
        ArticleStatisticEntity articleStatisticEntity1 = getArticleStatisticEntitySample1();
        ArticleStatisticEntity articleStatisticEntity2 = new ArticleStatisticEntity();
        assertThat(articleStatisticEntity1).isNotEqualTo(articleStatisticEntity2);

        articleStatisticEntity2.setId(articleStatisticEntity1.getId());
        assertThat(articleStatisticEntity1).isEqualTo(articleStatisticEntity2);

        articleStatisticEntity2 = getArticleStatisticEntitySample2();
        assertThat(articleStatisticEntity1).isNotEqualTo(articleStatisticEntity2);
    }
}
