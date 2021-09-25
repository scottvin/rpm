package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmCharacterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmCharacter} and its DTO {@link RpmCharacterDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmResultMapper.class })
public interface RpmCharacterMapper extends EntityMapper<RpmCharacterDTO, RpmCharacter> {
    @Mapping(target = "result", source = "result", qualifiedByName = "name")
    RpmCharacterDTO toDto(RpmCharacter s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RpmCharacterDTO toDtoName(RpmCharacter rpmCharacter);
}
