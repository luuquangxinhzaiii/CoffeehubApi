package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.ArticleBodyEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleBodyEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleBodyEntity.class);
        ArticleBodyEntity articleBodyEntity1 = getArticleBodyEntitySample1();
        ArticleBodyEntity articleBodyEntity2 = new ArticleBodyEntity();
        assertThat(articleBodyEntity1).isNotEqualTo(articleBodyEntity2);

        articleBodyEntity2.setId(articleBodyEntity1.getId());
        assertThat(articleBodyEntity1).isEqualTo(articleBodyEntity2);

        articleBodyEntity2 = getArticleBodyEntitySample2();
        assertThat(articleBodyEntity1).isNotEqualTo(articleBodyEntity2);
    }
}
