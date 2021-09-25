package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPlanDTO.class);
        RpmPlanDTO rpmPlanDTO1 = new RpmPlanDTO();
        rpmPlanDTO1.setId(1L);
        RpmPlanDTO rpmPlanDTO2 = new RpmPlanDTO();
        assertThat(rpmPlanDTO1).isNotEqualTo(rpmPlanDTO2);
        rpmPlanDTO2.setId(rpmPlanDTO1.getId());
        assertThat(rpmPlanDTO1).isEqualTo(rpmPlanDTO2);
        rpmPlanDTO2.setId(2L);
        assertThat(rpmPlanDTO1).isNotEqualTo(rpmPlanDTO2);
        rpmPlanDTO1.setId(null);
        assertThat(rpmPlanDTO1).isNotEqualTo(rpmPlanDTO2);
    }
}
