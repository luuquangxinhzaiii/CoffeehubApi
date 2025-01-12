package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.TimelineEntity;
import com.ali.coffeehub.brew.service.dto.TimelineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimelineEntity} and its DTO {@link TimelineDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimelineMapper extends EntityMapper<TimelineDTO, TimelineEntity> {}
