package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.BrewEntity;
import com.ali.coffeehub.brew.service.dto.BrewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BrewEntity} and its DTO {@link BrewDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrewMapper extends EntityMapper<BrewDTO, BrewEntity> {}
