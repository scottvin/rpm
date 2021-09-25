package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmReasonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmReason} and its DTO {@link RpmReasonDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmReasonMapper extends EntityMapper<RpmReasonDTO, RpmReason> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmReasonDTO toDtoName(RpmReason rpmReason);
}
