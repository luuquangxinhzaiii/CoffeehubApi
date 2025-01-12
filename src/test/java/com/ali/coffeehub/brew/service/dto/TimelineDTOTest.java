package com.ali.coffeehub.brew.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimelineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimelineDTO.class);
        TimelineDTO timelineDTO1 = new TimelineDTO();
        timelineDTO1.setId(1L);
        TimelineDTO timelineDTO2 = new TimelineDTO();
        assertThat(timelineDTO1).isNotEqualTo(timelineDTO2);
        timelineDTO2.setId(timelineDTO1.getId());
        assertThat(timelineDTO1).isEqualTo(timelineDTO2);
        timelineDTO2.setId(2L);
        assertThat(timelineDTO1).isNotEqualTo(timelineDTO2);
        timelineDTO1.setId(null);
        assertThat(timelineDTO1).isNotEqualTo(timelineDTO2);
    }
}
