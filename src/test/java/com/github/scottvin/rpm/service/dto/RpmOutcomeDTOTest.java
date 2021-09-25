package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmOutcomeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmOutcomeDTO.class);
        RpmOutcomeDTO rpmOutcomeDTO1 = new RpmOutcomeDTO();
        rpmOutcomeDTO1.setId(1L);
        RpmOutcomeDTO rpmOutcomeDTO2 = new RpmOutcomeDTO();
        assertThat(rpmOutcomeDTO1).isNotEqualTo(rpmOutcomeDTO2);
        rpmOutcomeDTO2.setId(rpmOutcomeDTO1.getId());
        assertThat(rpmOutcomeDTO1).isEqualTo(rpmOutcomeDTO2);
        rpmOutcomeDTO2.setId(2L);
        assertThat(rpmOutcomeDTO1).isNotEqualTo(rpmOutcomeDTO2);
        rpmOutcomeDTO1.setId(null);
        assertThat(rpmOutcomeDTO1).isNotEqualTo(rpmOutcomeDTO2);
    }
}
