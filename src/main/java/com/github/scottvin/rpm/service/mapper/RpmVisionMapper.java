package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmVisionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmVision} and its DTO {@link RpmVisionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmVisionMapper extends EntityMapper<RpmVisionDTO, RpmVision> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmVisionDTO toDtoName(RpmVision rpmVision);
}
