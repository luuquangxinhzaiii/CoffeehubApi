package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.ToolEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.ToolEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolMapperTest {

    private ToolMapper toolMapper;

    @BeforeEach
    void setUp() {
        toolMapper = new ToolMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getToolEntitySample1();
        var actual = toolMapper.toEntity(toolMapper.toDto(expected));
        assertToolEntityAllPropertiesEquals(expected, actual);
    }
}
