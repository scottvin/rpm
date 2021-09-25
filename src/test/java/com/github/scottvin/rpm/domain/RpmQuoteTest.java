package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmQuoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmQuote.class);
        RpmQuote rpmQuote1 = new RpmQuote();
        rpmQuote1.setId(1L);
        RpmQuote rpmQuote2 = new RpmQuote();
        rpmQuote2.setId(rpmQuote1.getId());
        assertThat(rpmQuote1).isEqualTo(rpmQuote2);
        rpmQuote2.setId(2L);
        assertThat(rpmQuote1).isNotEqualTo(rpmQuote2);
        rpmQuote1.setId(null);
        assertThat(rpmQuote1).isNotEqualTo(rpmQuote2);
    }
}
