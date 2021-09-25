package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmQuoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmQuoteDTO.class);
        RpmQuoteDTO rpmQuoteDTO1 = new RpmQuoteDTO();
        rpmQuoteDTO1.setId(1L);
        RpmQuoteDTO rpmQuoteDTO2 = new RpmQuoteDTO();
        assertThat(rpmQuoteDTO1).isNotEqualTo(rpmQuoteDTO2);
        rpmQuoteDTO2.setId(rpmQuoteDTO1.getId());
        assertThat(rpmQuoteDTO1).isEqualTo(rpmQuoteDTO2);
        rpmQuoteDTO2.setId(2L);
        assertThat(rpmQuoteDTO1).isNotEqualTo(rpmQuoteDTO2);
        rpmQuoteDTO1.setId(null);
        assertThat(rpmQuoteDTO1).isNotEqualTo(rpmQuoteDTO2);
    }
}
