package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmAction} and its DTO {@link RpmActionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { RpmPlanMapper.class, RpmReasonMapper.class, RpmCaptureMapper.class, RpmResultMapper.class, RpmProjectMapper.class }
)
public interface RpmActionMapper extends EntityMapper<RpmActionDTO, RpmAction> {
    @Mapping(target = "plan", source = "plan", qualifiedByName = "name")
    @Mapping(target = "reason", source = "reason", qualifiedByName = "name")
    @Mapping(target = "captures", source = "captures", qualifiedByName = "name")
    @Mapping(target = "result", source = "result", qualifiedByName = "name")
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    RpmActionDTO toDto(RpmAction s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmActionDTO toDtoName(RpmAction rpmAction);
}
