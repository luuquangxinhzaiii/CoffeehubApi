package com.ali.coffeehub.service.mapper;

import com.ali.coffeehub.domain.TagsEntity;
import com.ali.coffeehub.service.dto.TagsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagsEntity} and its DTO {@link TagsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagsMapper extends EntityMapper<TagsDTO, TagsEntity> {}
