package com.ali.coffeehub.roaster.service.mapper;

import static com.ali.coffeehub.roaster.domain.RoasterEntityAsserts.*;
import static com.ali.coffeehub.roaster.domain.RoasterEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoasterMapperTest {

    private RoasterMapper roasterMapper;

    @BeforeEach
    void setUp() {
        roasterMapper = new RoasterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoasterEntitySample1();
        var actual = roasterMapper.toEntity(roasterMapper.toDto(expected));
        assertRoasterEntityAllPropertiesEquals(expected, actual);
    }
}
