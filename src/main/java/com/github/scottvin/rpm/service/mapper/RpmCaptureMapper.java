package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmCaptureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmCapture} and its DTO {@link RpmCaptureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmCaptureMapper extends EntityMapper<RpmCaptureDTO, RpmCapture> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmCaptureDTO toDtoName(RpmCapture rpmCapture);
}
