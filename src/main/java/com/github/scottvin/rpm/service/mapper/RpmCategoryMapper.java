package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmCategory} and its DTO {@link RpmCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmCategoryMapper extends EntityMapper<RpmCategoryDTO, RpmCategory> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmCategoryDTO toDtoName(RpmCategory rpmCategory);
}
