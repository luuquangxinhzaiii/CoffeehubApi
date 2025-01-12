package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.ConfigValueEntity;
import com.ali.coffeehub.brew.service.dto.ConfigValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigValueEntity} and its DTO {@link ConfigValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigValueMapper extends EntityMapper<ConfigValueDTO, ConfigValueEntity> {}
