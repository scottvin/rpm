package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmAspectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmAspectDTO.class);
        RpmAspectDTO rpmAspectDTO1 = new RpmAspectDTO();
        rpmAspectDTO1.setId(1L);
        RpmAspectDTO rpmAspectDTO2 = new RpmAspectDTO();
        assertThat(rpmAspectDTO1).isNotEqualTo(rpmAspectDTO2);
        rpmAspectDTO2.setId(rpmAspectDTO1.getId());
        assertThat(rpmAspectDTO1).isEqualTo(rpmAspectDTO2);
        rpmAspectDTO2.setId(2L);
        assertThat(rpmAspectDTO1).isNotEqualTo(rpmAspectDTO2);
        rpmAspectDTO1.setId(null);
        assertThat(rpmAspectDTO1).isNotEqualTo(rpmAspectDTO2);
    }
}
