package com.ali.coffeehub.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntityTagsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityTagsDTO.class);
        EntityTagsDTO entityTagsDTO1 = new EntityTagsDTO();
        entityTagsDTO1.setId(1L);
        EntityTagsDTO entityTagsDTO2 = new EntityTagsDTO();
        assertThat(entityTagsDTO1).isNotEqualTo(entityTagsDTO2);
        entityTagsDTO2.setId(entityTagsDTO1.getId());
        assertThat(entityTagsDTO1).isEqualTo(entityTagsDTO2);
        entityTagsDTO2.setId(2L);
        assertThat(entityTagsDTO1).isNotEqualTo(entityTagsDTO2);
        entityTagsDTO1.setId(null);
        assertThat(entityTagsDTO1).isNotEqualTo(entityTagsDTO2);
    }
}
