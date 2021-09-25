package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmPlan} and its DTO {@link RpmPlanDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmProjectMapper.class })
public interface RpmPlanMapper extends EntityMapper<RpmPlanDTO, RpmPlan> {
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    RpmPlanDTO toDto(RpmPlan s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmPlanDTO toDtoName(RpmPlan rpmPlan);
}
