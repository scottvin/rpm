package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmReasonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmReasonDTO.class);
        RpmReasonDTO rpmReasonDTO1 = new RpmReasonDTO();
        rpmReasonDTO1.setId(1L);
        RpmReasonDTO rpmReasonDTO2 = new RpmReasonDTO();
        assertThat(rpmReasonDTO1).isNotEqualTo(rpmReasonDTO2);
        rpmReasonDTO2.setId(rpmReasonDTO1.getId());
        assertThat(rpmReasonDTO1).isEqualTo(rpmReasonDTO2);
        rpmReasonDTO2.setId(2L);
        assertThat(rpmReasonDTO1).isNotEqualTo(rpmReasonDTO2);
        rpmReasonDTO1.setId(null);
        assertThat(rpmReasonDTO1).isNotEqualTo(rpmReasonDTO2);
    }
}
