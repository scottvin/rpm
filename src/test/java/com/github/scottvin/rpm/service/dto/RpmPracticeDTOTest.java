package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPracticeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPracticeDTO.class);
        RpmPracticeDTO rpmPracticeDTO1 = new RpmPracticeDTO();
        rpmPracticeDTO1.setId(1L);
        RpmPracticeDTO rpmPracticeDTO2 = new RpmPracticeDTO();
        assertThat(rpmPracticeDTO1).isNotEqualTo(rpmPracticeDTO2);
        rpmPracticeDTO2.setId(rpmPracticeDTO1.getId());
        assertThat(rpmPracticeDTO1).isEqualTo(rpmPracticeDTO2);
        rpmPracticeDTO2.setId(2L);
        assertThat(rpmPracticeDTO1).isNotEqualTo(rpmPracticeDTO2);
        rpmPracticeDTO1.setId(null);
        assertThat(rpmPracticeDTO1).isNotEqualTo(rpmPracticeDTO2);
    }
}
