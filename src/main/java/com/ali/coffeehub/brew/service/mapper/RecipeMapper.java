package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.RecipeEntity;
import com.ali.coffeehub.brew.service.dto.RecipeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecipeEntity} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, RecipeEntity> {}
