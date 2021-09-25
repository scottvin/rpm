package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmCaptureMapperTest {

    private RpmCaptureMapper rpmCaptureMapper;

    @BeforeEach
    public void setUp() {
        rpmCaptureMapper = new RpmCaptureMapperImpl();
    }
}
