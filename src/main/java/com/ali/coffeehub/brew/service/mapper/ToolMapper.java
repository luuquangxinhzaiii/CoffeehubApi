package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.ToolEntity;
import com.ali.coffeehub.brew.service.dto.ToolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolEntity} and its DTO {@link ToolDTO}.
 */
@Mapper(componentModel = "spring")
public interface ToolMapper extends EntityMapper<ToolDTO, ToolEntity> {}
