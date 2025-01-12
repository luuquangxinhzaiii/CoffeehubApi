package com.ali.coffeehub.service.mapper;

import static com.ali.coffeehub.domain.EntityTagsEntityAsserts.*;
import static com.ali.coffeehub.domain.EntityTagsEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityTagsMapperTest {

    private EntityTagsMapper entityTagsMapper;

    @BeforeEach
    void setUp() {
        entityTagsMapper = new EntityTagsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntityTagsEntitySample1();
        var actual = entityTagsMapper.toEntity(entityTagsMapper.toDto(expected));
        assertEntityTagsEntityAllPropertiesEquals(expected, actual);
    }
}
