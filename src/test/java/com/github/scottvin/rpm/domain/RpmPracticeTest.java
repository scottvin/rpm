package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPracticeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPractice.class);
        RpmPractice rpmPractice1 = new RpmPractice();
        rpmPractice1.setId(1L);
        RpmPractice rpmPractice2 = new RpmPractice();
        rpmPractice2.setId(rpmPractice1.getId());
        assertThat(rpmPractice1).isEqualTo(rpmPractice2);
        rpmPractice2.setId(2L);
        assertThat(rpmPractice1).isNotEqualTo(rpmPractice2);
        rpmPractice1.setId(null);
        assertThat(rpmPractice1).isNotEqualTo(rpmPractice2);
    }
}
