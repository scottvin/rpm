package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCharacterGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCharacterGroup.class);
        RpmCharacterGroup rpmCharacterGroup1 = new RpmCharacterGroup();
        rpmCharacterGroup1.setId(1L);
        RpmCharacterGroup rpmCharacterGroup2 = new RpmCharacterGroup();
        rpmCharacterGroup2.setId(rpmCharacterGroup1.getId());
        assertThat(rpmCharacterGroup1).isEqualTo(rpmCharacterGroup2);
        rpmCharacterGroup2.setId(2L);
        assertThat(rpmCharacterGroup1).isNotEqualTo(rpmCharacterGroup2);
        rpmCharacterGroup1.setId(null);
        assertThat(rpmCharacterGroup1).isNotEqualTo(rpmCharacterGroup2);
    }
}
