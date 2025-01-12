package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.StepEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.StepEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StepMapperTest {

    private StepMapper stepMapper;

    @BeforeEach
    void setUp() {
        stepMapper = new StepMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStepEntitySample1();
        var actual = stepMapper.toEntity(stepMapper.toDto(expected));
        assertStepEntityAllPropertiesEquals(expected, actual);
    }
}
