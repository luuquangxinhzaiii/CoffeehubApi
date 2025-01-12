package com.ali.coffeehub.article.domain;

import static com.ali.coffeehub.article.domain.UserArticleInteractionEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserArticleInteractionEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserArticleInteractionEntity.class);
        UserArticleInteractionEntity userArticleInteractionEntity1 = getUserArticleInteractionEntitySample1();
        UserArticleInteractionEntity userArticleInteractionEntity2 = new UserArticleInteractionEntity();
        assertThat(userArticleInteractionEntity1).isNotEqualTo(userArticleInteractionEntity2);

        userArticleInteractionEntity2.setId(userArticleInteractionEntity1.getId());
        assertThat(userArticleInteractionEntity1).isEqualTo(userArticleInteractionEntity2);

        userArticleInteractionEntity2 = getUserArticleInteractionEntitySample2();
        assertThat(userArticleInteractionEntity1).isNotEqualTo(userArticleInteractionEntity2);
    }
}
