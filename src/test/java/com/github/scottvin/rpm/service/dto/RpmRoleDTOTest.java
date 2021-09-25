package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmRoleDTO.class);
        RpmRoleDTO rpmRoleDTO1 = new RpmRoleDTO();
        rpmRoleDTO1.setId(1L);
        RpmRoleDTO rpmRoleDTO2 = new RpmRoleDTO();
        assertThat(rpmRoleDTO1).isNotEqualTo(rpmRoleDTO2);
        rpmRoleDTO2.setId(rpmRoleDTO1.getId());
        assertThat(rpmRoleDTO1).isEqualTo(rpmRoleDTO2);
        rpmRoleDTO2.setId(2L);
        assertThat(rpmRoleDTO1).isNotEqualTo(rpmRoleDTO2);
        rpmRoleDTO1.setId(null);
        assertThat(rpmRoleDTO1).isNotEqualTo(rpmRoleDTO2);
    }
}
