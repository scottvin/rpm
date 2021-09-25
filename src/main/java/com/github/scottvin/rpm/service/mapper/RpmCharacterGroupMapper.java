package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmCharacterGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmCharacterGroup} and its DTO {@link RpmCharacterGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpmCharacterGroupMapper extends EntityMapper<RpmCharacterGroupDTO, RpmCharacterGroup> {}
