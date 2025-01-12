package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.ConfigEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.ConfigEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigMapperTest {

    private ConfigMapper configMapper;

    @BeforeEach
    void setUp() {
        configMapper = new ConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConfigEntitySample1();
        var actual = configMapper.toEntity(configMapper.toDto(expected));
        assertConfigEntityAllPropertiesEquals(expected, actual);
    }
}
