package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmActionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmActionDTO.class);
        RpmActionDTO rpmActionDTO1 = new RpmActionDTO();
        rpmActionDTO1.setId(1L);
        RpmActionDTO rpmActionDTO2 = new RpmActionDTO();
        assertThat(rpmActionDTO1).isNotEqualTo(rpmActionDTO2);
        rpmActionDTO2.setId(rpmActionDTO1.getId());
        assertThat(rpmActionDTO1).isEqualTo(rpmActionDTO2);
        rpmActionDTO2.setId(2L);
        assertThat(rpmActionDTO1).isNotEqualTo(rpmActionDTO2);
        rpmActionDTO1.setId(null);
        assertThat(rpmActionDTO1).isNotEqualTo(rpmActionDTO2);
    }
}
