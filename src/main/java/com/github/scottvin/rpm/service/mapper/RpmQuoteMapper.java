package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmQuoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmQuote} and its DTO {@link RpmQuoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { RpmCharacterMapper.class })
public interface RpmQuoteMapper extends EntityMapper<RpmQuoteDTO, RpmQuote> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    RpmQuoteDTO toDto(RpmQuote s);
}
