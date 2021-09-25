package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPurposeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPurposeDTO.class);
        RpmPurposeDTO rpmPurposeDTO1 = new RpmPurposeDTO();
        rpmPurposeDTO1.setId(1L);
        RpmPurposeDTO rpmPurposeDTO2 = new RpmPurposeDTO();
        assertThat(rpmPurposeDTO1).isNotEqualTo(rpmPurposeDTO2);
        rpmPurposeDTO2.setId(rpmPurposeDTO1.getId());
        assertThat(rpmPurposeDTO1).isEqualTo(rpmPurposeDTO2);
        rpmPurposeDTO2.setId(2L);
        assertThat(rpmPurposeDTO1).isNotEqualTo(rpmPurposeDTO2);
        rpmPurposeDTO1.setId(null);
        assertThat(rpmPurposeDTO1).isNotEqualTo(rpmPurposeDTO2);
    }
}
