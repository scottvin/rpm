package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCaptureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCaptureDTO.class);
        RpmCaptureDTO rpmCaptureDTO1 = new RpmCaptureDTO();
        rpmCaptureDTO1.setId(1L);
        RpmCaptureDTO rpmCaptureDTO2 = new RpmCaptureDTO();
        assertThat(rpmCaptureDTO1).isNotEqualTo(rpmCaptureDTO2);
        rpmCaptureDTO2.setId(rpmCaptureDTO1.getId());
        assertThat(rpmCaptureDTO1).isEqualTo(rpmCaptureDTO2);
        rpmCaptureDTO2.setId(2L);
        assertThat(rpmCaptureDTO1).isNotEqualTo(rpmCaptureDTO2);
        rpmCaptureDTO1.setId(null);
        assertThat(rpmCaptureDTO1).isNotEqualTo(rpmCaptureDTO2);
    }
}
