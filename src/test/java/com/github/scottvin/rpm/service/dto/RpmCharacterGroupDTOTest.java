package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCharacterGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCharacterGroupDTO.class);
        RpmCharacterGroupDTO rpmCharacterGroupDTO1 = new RpmCharacterGroupDTO();
        rpmCharacterGroupDTO1.setId(1L);
        RpmCharacterGroupDTO rpmCharacterGroupDTO2 = new RpmCharacterGroupDTO();
        assertThat(rpmCharacterGroupDTO1).isNotEqualTo(rpmCharacterGroupDTO2);
        rpmCharacterGroupDTO2.setId(rpmCharacterGroupDTO1.getId());
        assertThat(rpmCharacterGroupDTO1).isEqualTo(rpmCharacterGroupDTO2);
        rpmCharacterGroupDTO2.setId(2L);
        assertThat(rpmCharacterGroupDTO1).isNotEqualTo(rpmCharacterGroupDTO2);
        rpmCharacterGroupDTO1.setId(null);
        assertThat(rpmCharacterGroupDTO1).isNotEqualTo(rpmCharacterGroupDTO2);
    }
}
