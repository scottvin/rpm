package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmOutcomeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmOutcome} and its DTO {@link RpmOutcomeDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmProjectMapper.class })
public interface RpmOutcomeMapper extends EntityMapper<RpmOutcomeDTO, RpmOutcome> {
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    RpmOutcomeDTO toDto(RpmOutcome s);
}
