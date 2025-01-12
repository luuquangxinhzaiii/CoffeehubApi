package com.ali.coffeehub.domain;

import static com.ali.coffeehub.domain.EntityTagsEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntityTagsEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityTagsEntity.class);
        EntityTagsEntity entityTagsEntity1 = getEntityTagsEntitySample1();
        EntityTagsEntity entityTagsEntity2 = new EntityTagsEntity();
        assertThat(entityTagsEntity1).isNotEqualTo(entityTagsEntity2);

        entityTagsEntity2.setId(entityTagsEntity1.getId());
        assertThat(entityTagsEntity1).isEqualTo(entityTagsEntity2);

        entityTagsEntity2 = getEntityTagsEntitySample2();
        assertThat(entityTagsEntity1).isNotEqualTo(entityTagsEntity2);
    }
}
