package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmPurposeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmPurpose} and its DTO {@link RpmPurposeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmPurposeMapper extends EntityMapper<RpmPurposeDTO, RpmPurpose> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmPurposeDTO toDtoName(RpmPurpose rpmPurpose);
}
