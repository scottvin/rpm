package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPurposeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPurpose.class);
        RpmPurpose rpmPurpose1 = new RpmPurpose();
        rpmPurpose1.setId(1L);
        RpmPurpose rpmPurpose2 = new RpmPurpose();
        rpmPurpose2.setId(rpmPurpose1.getId());
        assertThat(rpmPurpose1).isEqualTo(rpmPurpose2);
        rpmPurpose2.setId(2L);
        assertThat(rpmPurpose1).isNotEqualTo(rpmPurpose2);
        rpmPurpose1.setId(null);
        assertThat(rpmPurpose1).isNotEqualTo(rpmPurpose2);
    }
}
