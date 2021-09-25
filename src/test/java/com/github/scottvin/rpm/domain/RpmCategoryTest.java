package com.github.scottvin.rpm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.scottvin.rpm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RpmCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RpmCategory.class);
        RpmCategory rpmCategory1 = new RpmCategory();
        rpmCategory1.setId(1L);
        RpmCategory rpmCategory2 = new RpmCategory();
        rpmCategory2.setId(rpmCategory1.getId());
        assertThat(rpmCategory1).isEqualTo(rpmCategory2);
        rpmCategory2.setId(2L);
        assertThat(rpmCategory1).isNotEqualTo(rpmCategory2);
        rpmCategory1.setId(null);
        assertThat(rpmCategory1).isNotEqualTo(rpmCategory2);
    }
}
