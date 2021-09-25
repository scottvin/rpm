package com.github.scottvin.rpm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RpmPlanMapperTest {

    private RpmPlanMapper rpmPlanMapper;

    @BeforeEach
    public void setUp() {
        rpmPlanMapper = new RpmPlanMapperImpl();
    }
}
