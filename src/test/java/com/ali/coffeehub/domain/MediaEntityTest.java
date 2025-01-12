package com.ali.coffeehub.domain;

import static com.ali.coffeehub.domain.MediaEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediaEntity.class);
        MediaEntity mediaEntity1 = getMediaEntitySample1();
        MediaEntity mediaEntity2 = new MediaEntity();
        assertThat(mediaEntity1).isNotEqualTo(mediaEntity2);

        mediaEntity2.setId(mediaEntity1.getId());
        assertThat(mediaEntity1).isEqualTo(mediaEntity2);

        mediaEntity2 = getMediaEntitySample2();
        assertThat(mediaEntity1).isNotEqualTo(mediaEntity2);
    }
}
