package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmResource.class);
        RpmResource rpmResource1 = new RpmResource();
        rpmResource1.setId(1L);
        RpmResource rpmResource2 = new RpmResource();
        rpmResource2.setId(rpmResource1.getId());
        assertThat(rpmResource1).isEqualTo(rpmResource2);
        rpmResource2.setId(2L);
        assertThat(rpmResource1).isNotEqualTo(rpmResource2);
        rpmResource1.setId(null);
        assertThat(rpmResource1).isNotEqualTo(rpmResource2);
    }
}
