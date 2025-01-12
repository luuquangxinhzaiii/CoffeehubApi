package com.ali.coffeehub.roaster.service.mapper;

import com.ali.coffeehub.roaster.domain.RoasterEntity;
import com.ali.coffeehub.roaster.service.dto.RoasterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoasterEntity} and its DTO {@link RoasterDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoasterMapper extends EntityMapper<RoasterDTO, RoasterEntity> {}
