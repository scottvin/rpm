package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmAspectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmAspect.class);
        RpmAspect rpmAspect1 = new RpmAspect();
        rpmAspect1.setId(1L);
        RpmAspect rpmAspect2 = new RpmAspect();
        rpmAspect2.setId(rpmAspect1.getId());
        assertThat(rpmAspect1).isEqualTo(rpmAspect2);
        rpmAspect2.setId(2L);
        assertThat(rpmAspect1).isNotEqualTo(rpmAspect2);
        rpmAspect1.setId(null);
        assertThat(rpmAspect1).isNotEqualTo(rpmAspect2);
    }
}
