package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.ConfigValueEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.ConfigValueEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigValueMapperTest {

    private ConfigValueMapper configValueMapper;

    @BeforeEach
    void setUp() {
        configValueMapper = new ConfigValueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConfigValueEntitySample1();
        var actual = configValueMapper.toEntity(configValueMapper.toDto(expected));
        assertConfigValueEntityAllPropertiesEquals(expected, actual);
    }
}
