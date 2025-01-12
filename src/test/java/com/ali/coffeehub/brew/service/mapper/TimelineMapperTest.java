package com.ali.coffeehub.brew.service.mapper;

import static com.ali.coffeehub.brew.domain.TimelineEntityAsserts.*;
import static com.ali.coffeehub.brew.domain.TimelineEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimelineMapperTest {

    private TimelineMapper timelineMapper;

    @BeforeEach
    void setUp() {
        timelineMapper = new TimelineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimelineEntitySample1();
        var actual = timelineMapper.toEntity(timelineMapper.toDto(expected));
        assertTimelineEntityAllPropertiesEquals(expected, actual);
    }
}
