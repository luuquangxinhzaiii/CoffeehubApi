package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.BrewEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.BrewEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrewMapperTest {

    private BrewMapper brewMapper;

    @BeforeEach
    void setUp() {
        brewMapper = new BrewMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBrewEntitySample1();
        var actual = brewMapper.toEntity(brewMapper.toDto(expected));
        assertBrewEntityAllPropertiesEquals(expected, actual);
    }
}
