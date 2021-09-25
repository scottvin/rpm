package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmRole.class);
        RpmRole rpmRole1 = new RpmRole();
        rpmRole1.setId(1L);
        RpmRole rpmRole2 = new RpmRole();
        rpmRole2.setId(rpmRole1.getId());
        assertThat(rpmRole1).isEqualTo(rpmRole2);
        rpmRole2.setId(2L);
        assertThat(rpmRole1).isNotEqualTo(rpmRole2);
        rpmRole1.setId(null);
        assertThat(rpmRole1).isNotEqualTo(rpmRole2);
    }
}
