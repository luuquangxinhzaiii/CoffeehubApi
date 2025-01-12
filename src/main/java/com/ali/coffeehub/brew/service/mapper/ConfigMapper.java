package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.ConfigEntity;
import com.ali.coffeehub.brew.service.dto.ConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigEntity} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper extends EntityMapper<ConfigDTO, ConfigEntity> {}
