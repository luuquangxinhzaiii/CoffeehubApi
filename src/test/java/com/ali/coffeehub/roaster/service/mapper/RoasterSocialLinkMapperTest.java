package com.ali.coffeehub.roaster.service.mapper;

import static com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntityAsserts.*;
import static com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoasterSocialLinkMapperTest {

    private RoasterSocialLinkMapper roasterSocialLinkMapper;

    @BeforeEach
    void setUp() {
        roasterSocialLinkMapper = new RoasterSocialLinkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoasterSocialLinkEntitySample1();
        var actual = roasterSocialLinkMapper.toEntity(roasterSocialLinkMapper.toDto(expected));
        assertRoasterSocialLinkEntityAllPropertiesEquals(expected, actual);
    }
}
