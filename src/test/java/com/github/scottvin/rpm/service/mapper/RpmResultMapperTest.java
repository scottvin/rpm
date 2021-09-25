package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmResultMapperTest {

    private RpmResultMapper rpmResultMapper;

    @BeforeEach
    public void setUp() {
        rpmResultMapper = new RpmResultMapperImpl();
    }
}
