package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmAction.class);
        RpmAction rpmAction1 = new RpmAction();
        rpmAction1.setId(1L);
        RpmAction rpmAction2 = new RpmAction();
        rpmAction2.setId(rpmAction1.getId());
        assertThat(rpmAction1).isEqualTo(rpmAction2);
        rpmAction2.setId(2L);
        assertThat(rpmAction1).isNotEqualTo(rpmAction2);
        rpmAction1.setId(null);
        assertThat(rpmAction1).isNotEqualTo(rpmAction2);
    }
}
