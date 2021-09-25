package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCategoryDTO.class);
        RpmCategoryDTO rpmCategoryDTO1 = new RpmCategoryDTO();
        rpmCategoryDTO1.setId(1L);
        RpmCategoryDTO rpmCategoryDTO2 = new RpmCategoryDTO();
        assertThat(rpmCategoryDTO1).isNotEqualTo(rpmCategoryDTO2);
        rpmCategoryDTO2.setId(rpmCategoryDTO1.getId());
        assertThat(rpmCategoryDTO1).isEqualTo(rpmCategoryDTO2);
        rpmCategoryDTO2.setId(2L);
        assertThat(rpmCategoryDTO1).isNotEqualTo(rpmCategoryDTO2);
        rpmCategoryDTO1.setId(null);
        assertThat(rpmCategoryDTO1).isNotEqualTo(rpmCategoryDTO2);
    }
}
