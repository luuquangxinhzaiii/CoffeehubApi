package com.ali.coffeehub.service.mapper;

import static com.ali.coffeehub.domain.MediaEntityAsserts.*;
import static com.ali.coffeehub.domain.MediaEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaMapperTest {

    private MediaMapper mediaMapper;

    @BeforeEach
    void setUp() {
        mediaMapper = new MediaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMediaEntitySample1();
        var actual = mediaMapper.toEntity(mediaMapper.toDto(expected));
        assertMediaEntityAllPropertiesEquals(expected, actual);
    }
}
