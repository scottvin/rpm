package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmPracticeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmPractice} and its DTO {@link RpmPracticeDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmCharacterMapper.class })
public interface RpmPracticeMapper extends EntityMapper<RpmPracticeDTO, RpmPractice> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    RpmPracticeDTO toDto(RpmPractice s);
}
