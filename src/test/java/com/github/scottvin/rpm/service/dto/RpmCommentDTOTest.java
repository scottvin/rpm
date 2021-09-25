package com.github.scottvin.rpm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCommentDTO.class);
        RpmCommentDTO rpmCommentDTO1 = new RpmCommentDTO();
        rpmCommentDTO1.setId(1L);
        RpmCommentDTO rpmCommentDTO2 = new RpmCommentDTO();
        assertThat(rpmCommentDTO1).isNotEqualTo(rpmCommentDTO2);
        rpmCommentDTO2.setId(rpmCommentDTO1.getId());
        assertThat(rpmCommentDTO1).isEqualTo(rpmCommentDTO2);
        rpmCommentDTO2.setId(2L);
        assertThat(rpmCommentDTO1).isNotEqualTo(rpmCommentDTO2);
        rpmCommentDTO1.setId(null);
        assertThat(rpmCommentDTO1).isNotEqualTo(rpmCommentDTO2);
    }
}
