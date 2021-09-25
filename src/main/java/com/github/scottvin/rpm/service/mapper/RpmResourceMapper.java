package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmResource} and its DTO {@link RpmResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmActionMapper.class })
public interface RpmResourceMapper extends EntityMapper<RpmResourceDTO, RpmResource> {
    @Mapping(target = "action", source = "action", qualifiedByName = "name")
    RpmResourceDTO toDto(RpmResource s);
}
