package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmProject} and its DTO {@link RpmProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmProjectMapper extends EntityMapper<RpmProjectDTO, RpmProject> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmProjectDTO toDtoName(RpmProject rpmProject);
}
