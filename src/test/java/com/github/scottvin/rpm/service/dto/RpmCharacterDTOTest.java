package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCharacterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCharacterDTO.class);
        RpmCharacterDTO rpmCharacterDTO1 = new RpmCharacterDTO();
        rpmCharacterDTO1.setId(1L);
        RpmCharacterDTO rpmCharacterDTO2 = new RpmCharacterDTO();
        assertThat(rpmCharacterDTO1).isNotEqualTo(rpmCharacterDTO2);
        rpmCharacterDTO2.setId(rpmCharacterDTO1.getId());
        assertThat(rpmCharacterDTO1).isEqualTo(rpmCharacterDTO2);
        rpmCharacterDTO2.setId(2L);
        assertThat(rpmCharacterDTO1).isNotEqualTo(rpmCharacterDTO2);
        rpmCharacterDTO1.setId(null);
        assertThat(rpmCharacterDTO1).isNotEqualTo(rpmCharacterDTO2);
    }
}
