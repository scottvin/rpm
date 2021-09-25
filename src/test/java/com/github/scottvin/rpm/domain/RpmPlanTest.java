package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmPlan.class);
        RpmPlan rpmPlan1 = new RpmPlan();
        rpmPlan1.setId(1L);
        RpmPlan rpmPlan2 = new RpmPlan();
        rpmPlan2.setId(rpmPlan1.getId());
        assertThat(rpmPlan1).isEqualTo(rpmPlan2);
        rpmPlan2.setId(2L);
        assertThat(rpmPlan1).isNotEqualTo(rpmPlan2);
        rpmPlan1.setId(null);
        assertThat(rpmPlan1).isNotEqualTo(rpmPlan2);
    }
}
