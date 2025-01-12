package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.StepEntity;
import com.ali.coffeehub.brew.service.dto.StepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StepEntity} and its DTO {@link StepDTO}.
 */
@Mapper(componentModel = "spring")
public interface StepMapper extends EntityMapper<StepDTO, StepEntity> {}
