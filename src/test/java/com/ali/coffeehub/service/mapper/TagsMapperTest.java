package com.ali.coffeehub.service.mapper;

import static com.ali.coffeehub.domain.TagsEntityAsserts.*;
import static com.ali.coffeehub.domain.TagsEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagsMapperTest {

    private TagsMapper tagsMapper;

    @BeforeEach
    void setUp() {
        tagsMapper = new TagsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTagsEntitySample1();
        var actual = tagsMapper.toEntity(tagsMapper.toDto(expected));
        assertTagsEntityAllPropertiesEquals(expected, actual);
    }
}
