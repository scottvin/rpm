package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmResultDTO.class);
        RpmResultDTO rpmResultDTO1 = new RpmResultDTO();
        rpmResultDTO1.setId(1L);
        RpmResultDTO rpmResultDTO2 = new RpmResultDTO();
        assertThat(rpmResultDTO1).isNotEqualTo(rpmResultDTO2);
        rpmResultDTO2.setId(rpmResultDTO1.getId());
        assertThat(rpmResultDTO1).isEqualTo(rpmResultDTO2);
        rpmResultDTO2.setId(2L);
        assertThat(rpmResultDTO1).isNotEqualTo(rpmResultDTO2);
        rpmResultDTO1.setId(null);
        assertThat(rpmResultDTO1).isNotEqualTo(rpmResultDTO2);
    }
}
