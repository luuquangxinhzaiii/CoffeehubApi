package com.ali.coffeehub.domain;

import static com.ali.coffeehub.domain.TagsEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagsEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagsEntity.class);
        TagsEntity tagsEntity1 = getTagsEntitySample1();
        TagsEntity tagsEntity2 = new TagsEntity();
        assertThat(tagsEntity1).isNotEqualTo(tagsEntity2);

        tagsEntity2.setId(tagsEntity1.getId());
        assertThat(tagsEntity1).isEqualTo(tagsEntity2);

        tagsEntity2 = getTagsEntitySample2();
        assertThat(tagsEntity1).isNotEqualTo(tagsEntity2);
    }
}
