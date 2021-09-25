package com.github.scottvin.rpm.service.mapper;

import com.github.scottvin.rpm.domain.*;
import com.github.scottvin.rpm.service.dto.RpmCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpmComment} and its DTO {@link RpmCommentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { RpmResultMapper.class, RpmProjectMapper.class, RpmActionMapper.class, RpmCharacterMapper.class }
)
public interface RpmCommentMapper extends EntityMapper<RpmCommentDTO, RpmComment> {
    @Mapping(target = "result", source = "result", qualifiedByName = "name")
    @Mapping(target = "project", source = "project", qualifiedByName = "name")
    @Mapping(target = "action", source = "action", qualifiedByName = "name")
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    RpmCommentDTO toDto(RpmComment s);
}
