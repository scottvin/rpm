package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmCharacterMapperTest {

    private RpmCharacterMapper rpmCharacterMapper;

    @BeforeEach
    public void setUp() {
        rpmCharacterMapper = new RpmCharacterMapperImpl();
    }
}
