package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmComment.class);
        RpmComment rpmComment1 = new RpmComment();
        rpmComment1.setId(1L);
        RpmComment rpmComment2 = new RpmComment();
        rpmComment2.setId(rpmComment1.getId());
        assertThat(rpmComment1).isEqualTo(rpmComment2);
        rpmComment2.setId(2L);
        assertThat(rpmComment1).isNotEqualTo(rpmComment2);
        rpmComment1.setId(null);
        assertThat(rpmComment1).isNotEqualTo(rpmComment2);
    }
}
