package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmReason.class);
        RpmReason rpmReason1 = new RpmReason();
        rpmReason1.setId(1L);
        RpmReason rpmReason2 = new RpmReason();
        rpmReason2.setId(rpmReason1.getId());
        assertThat(rpmReason1).isEqualTo(rpmReason2);
        rpmReason2.setId(2L);
        assertThat(rpmReason1).isNotEqualTo(rpmReason2);
        rpmReason1.setId(null);
        assertThat(rpmReason1).isNotEqualTo(rpmReason2);
    }
}
