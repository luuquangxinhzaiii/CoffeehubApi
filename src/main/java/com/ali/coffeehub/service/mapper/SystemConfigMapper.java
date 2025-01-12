package com.ali.coffeehub.service.mapper;

import com.ali.coffeehub.domain.SystemConfigEntity;
import com.ali.coffeehub.service.dto.SystemConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemConfigEntity} and its DTO {@link SystemConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemConfigMapper extends EntityMapper<SystemConfigDTO, SystemConfigEntity> {}
