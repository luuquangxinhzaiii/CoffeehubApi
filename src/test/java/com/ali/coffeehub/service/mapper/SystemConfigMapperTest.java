package com.ali.coffeehub.service.mapper;

import static com.ali.coffeehub.domain.SystemConfigEntityAsserts.*;
import static com.ali.coffeehub.domain.SystemConfigEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemConfigMapperTest {

    private SystemConfigMapper systemConfigMapper;

    @BeforeEach
    void setUp() {
        systemConfigMapper = new SystemConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemConfigEntitySample1();
        var actual = systemConfigMapper.toEntity(systemConfigMapper.toDto(expected));
        assertSystemConfigEntityAllPropertiesEquals(expected, actual);
    }
}
