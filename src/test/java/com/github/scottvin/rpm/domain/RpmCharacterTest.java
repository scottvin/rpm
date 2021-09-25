package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCharacterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCharacter.class);
        RpmCharacter rpmCharacter1 = new RpmCharacter();
        rpmCharacter1.setId(1L);
        RpmCharacter rpmCharacter2 = new RpmCharacter();
        rpmCharacter2.setId(rpmCharacter1.getId());
        assertThat(rpmCharacter1).isEqualTo(rpmCharacter2);
        rpmCharacter2.setId(2L);
        assertThat(rpmCharacter1).isNotEqualTo(rpmCharacter2);
        rpmCharacter1.setId(null);
        assertThat(rpmCharacter1).isNotEqualTo(rpmCharacter2);
    }
}
