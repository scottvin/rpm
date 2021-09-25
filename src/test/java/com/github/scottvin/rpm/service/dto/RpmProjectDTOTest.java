package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmProjectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmProjectDTO.class);
        RpmProjectDTO rpmProjectDTO1 = new RpmProjectDTO();
        rpmProjectDTO1.setId(1L);
        RpmProjectDTO rpmProjectDTO2 = new RpmProjectDTO();
        assertThat(rpmProjectDTO1).isNotEqualTo(rpmProjectDTO2);
        rpmProjectDTO2.setId(rpmProjectDTO1.getId());
        assertThat(rpmProjectDTO1).isEqualTo(rpmProjectDTO2);
        rpmProjectDTO2.setId(2L);
        assertThat(rpmProjectDTO1).isNotEqualTo(rpmProjectDTO2);
        rpmProjectDTO1.setId(null);
        assertThat(rpmProjectDTO1).isNotEqualTo(rpmProjectDTO2);
    }
}
