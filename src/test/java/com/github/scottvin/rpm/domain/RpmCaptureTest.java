package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCaptureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCapture.class);
        RpmCapture rpmCapture1 = new RpmCapture();
        rpmCapture1.setId(1L);
        RpmCapture rpmCapture2 = new RpmCapture();
        rpmCapture2.setId(rpmCapture1.getId());
        assertThat(rpmCapture1).isEqualTo(rpmCapture2);
        rpmCapture2.setId(2L);
        assertThat(rpmCapture1).isNotEqualTo(rpmCapture2);
        rpmCapture1.setId(null);
        assertThat(rpmCapture1).isNotEqualTo(rpmCapture2);
    }
}
