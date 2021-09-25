package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmVisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmVisionDTO.class);
        RpmVisionDTO rpmVisionDTO1 = new RpmVisionDTO();
        rpmVisionDTO1.setId(1L);
        RpmVisionDTO rpmVisionDTO2 = new RpmVisionDTO();
        assertThat(rpmVisionDTO1).isNotEqualTo(rpmVisionDTO2);
        rpmVisionDTO2.setId(rpmVisionDTO1.getId());
        assertThat(rpmVisionDTO1).isEqualTo(rpmVisionDTO2);
        rpmVisionDTO2.setId(2L);
        assertThat(rpmVisionDTO1).isNotEqualTo(rpmVisionDTO2);
        rpmVisionDTO1.setId(null);
        assertThat(rpmVisionDTO1).isNotEqualTo(rpmVisionDTO2);
    }
}
