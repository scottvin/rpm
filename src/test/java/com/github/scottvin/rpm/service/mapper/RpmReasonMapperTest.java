package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmReasonMapperTest {

    private RpmReasonMapper rpmReasonMapper;

    @BeforeEach
    public void setUp() {
        rpmReasonMapper = new RpmReasonMapperImpl();
    }
}
