package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmRole} and its DTO {@link RpmRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmRoleMapper extends EntityMapper<RpmRoleDTO, RpmRole> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmRoleDTO toDtoName(RpmRole rpmRole);
}
