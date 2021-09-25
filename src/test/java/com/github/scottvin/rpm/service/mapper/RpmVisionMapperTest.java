package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmVisionMapperTest {

    private RpmVisionMapper rpmVisionMapper;

    @BeforeEach
    public void setUp() {
        rpmVisionMapper = new RpmVisionMapperImpl();
    }
}
