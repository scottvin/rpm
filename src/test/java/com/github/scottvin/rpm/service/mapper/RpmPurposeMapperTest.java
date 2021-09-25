package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmPurposeMapperTest {

    private RpmPurposeMapper rpmPurposeMapper;

    @BeforeEach
    public void setUp() {
        rpmPurposeMapper = new RpmPurposeMapperImpl();
    }
}
