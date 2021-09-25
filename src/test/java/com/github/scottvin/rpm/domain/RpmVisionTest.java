package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmVisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmVision.class);
        RpmVision rpmVision1 = new RpmVision();
        rpmVision1.setId(1L);
        RpmVision rpmVision2 = new RpmVision();
        rpmVision2.setId(rpmVision1.getId());
        assertThat(rpmVision1).isEqualTo(rpmVision2);
        rpmVision2.setId(2L);
        assertThat(rpmVision1).isNotEqualTo(rpmVision2);
        rpmVision1.setId(null);
        assertThat(rpmVision1).isNotEqualTo(rpmVision2);
    }
}
