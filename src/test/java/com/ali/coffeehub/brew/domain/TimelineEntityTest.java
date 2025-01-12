package com.ali.coffeehub.brew.domain;

import static com.ali.coffeehub.brew.domain.TimelineEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimelineEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimelineEntity.class);
        TimelineEntity timelineEntity1 = getTimelineEntitySample1();
        TimelineEntity timelineEntity2 = new TimelineEntity();
        assertThat(timelineEntity1).isNotEqualTo(timelineEntity2);

        timelineEntity2.setId(timelineEntity1.getId());
        assertThat(timelineEntity1).isEqualTo(timelineEntity2);

        timelineEntity2 = getTimelineEntitySample2();
        assertThat(timelineEntity1).isNotEqualTo(timelineEntity2);
    }
}
