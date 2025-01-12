package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity;
import com.ali.coffeehub.brew.service.dto.RecipeDetailMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecipeDetailMediaEntity} and its DTO {@link RecipeDetailMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeDetailMediaMapper extends EntityMapper<RecipeDetailMediaDTO, RecipeDetailMediaEntity> {}
