package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmOutcomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmOutcome.class);
        RpmOutcome rpmOutcome1 = new RpmOutcome();
        rpmOutcome1.setId(1L);
        RpmOutcome rpmOutcome2 = new RpmOutcome();
        rpmOutcome2.setId(rpmOutcome1.getId());
        assertThat(rpmOutcome1).isEqualTo(rpmOutcome2);
        rpmOutcome2.setId(2L);
        assertThat(rpmOutcome1).isNotEqualTo(rpmOutcome2);
        rpmOutcome1.setId(null);
        assertThat(rpmOutcome1).isNotEqualTo(rpmOutcome2);
    }
}
