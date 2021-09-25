package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmResult} and its DTO {@link RpmResultDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { RpmCategoryMapper.class, RpmAspectMapper.class, RpmVisionMapper.class, RpmPurposeMapper.class, RpmRoleMapper.class }
)
public interface RpmResultMapper extends EntityMapper<RpmResultDTO, RpmResult> {
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    @Mapping(target = "aspect", source = "aspect", qualifiedByName = "name")
    @Mapping(target = "vision", source = "vision", qualifiedByName = "name")
    @Mapping(target = "purpose", source = "purpose", qualifiedByName = "name")
    @Mapping(target = "role", source = "role", qualifiedByName = "name")
    RpmResultDTO toDto(RpmResult s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmResultDTO toDtoName(RpmResult rpmResult);
}
