package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmCommentMapperTest {

    private RpmCommentMapper rpmCommentMapper;

    @BeforeEach
    public void setUp() {
        rpmCommentMapper = new RpmCommentMapperImpl();
    }
}
