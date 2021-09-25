package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmResourceDTO.class);
        RpmResourceDTO rpmResourceDTO1 = new RpmResourceDTO();
        rpmResourceDTO1.setId(1L);
        RpmResourceDTO rpmResourceDTO2 = new RpmResourceDTO();
        assertThat(rpmResourceDTO1).isNotEqualTo(rpmResourceDTO2);
        rpmResourceDTO2.setId(rpmResourceDTO1.getId());
        assertThat(rpmResourceDTO1).isEqualTo(rpmResourceDTO2);
        rpmResourceDTO2.setId(2L);
        assertThat(rpmResourceDTO1).isNotEqualTo(rpmResourceDTO2);
        rpmResourceDTO1.setId(null);
        assertThat(rpmResourceDTO1).isNotEqualTo(rpmResourceDTO2);
    }
}
