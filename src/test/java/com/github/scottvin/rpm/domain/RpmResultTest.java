package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmResult.class);
        RpmResult rpmResult1 = new RpmResult();
        rpmResult1.setId(1L);
        RpmResult rpmResult2 = new RpmResult();
        rpmResult2.setId(rpmResult1.getId());
        assertThat(rpmResult1).isEqualTo(rpmResult2);
        rpmResult2.setId(2L);
        assertThat(rpmResult1).isNotEqualTo(rpmResult2);
        rpmResult1.setId(null);
        assertThat(rpmResult1).isNotEqualTo(rpmResult2);
    }
}
