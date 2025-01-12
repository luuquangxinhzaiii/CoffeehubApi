package com.ali.coffeehub.service.mapper;

import com.ali.coffeehub.domain.MediaEntity;
import com.ali.coffeehub.service.dto.MediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MediaEntity} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, MediaEntity> {}
