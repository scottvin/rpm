package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmProject.class);
        RpmProject rpmProject1 = new RpmProject();
        rpmProject1.setId(1L);
        RpmProject rpmProject2 = new RpmProject();
        rpmProject2.setId(rpmProject1.getId());
        assertThat(rpmProject1).isEqualTo(rpmProject2);
        rpmProject2.setId(2L);
        assertThat(rpmProject1).isNotEqualTo(rpmProject2);
        rpmProject1.setId(null);
        assertThat(rpmProject1).isNotEqualTo(rpmProject2);
    }
}
