package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmAspectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmAspect} and its DTO {@link RpmAspectDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmAspectMapper extends EntityMapper<RpmAspectDTO, RpmAspect> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmAspectDTO toDtoName(RpmAspect rpmAspect);
}
