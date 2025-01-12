package com.ali.coffeehub.service.mapper;

import com.ali.coffeehub.domain.EntityTagsEntity;
import com.ali.coffeehub.service.dto.EntityTagsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EntityTagsEntity} and its DTO {@link EntityTagsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntityTagsMapper extends EntityMapper<EntityTagsDTO, EntityTagsEntity> {}
