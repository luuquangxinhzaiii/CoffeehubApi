package com.ali.coffeehub.brew.service.mapper;

import com.ali.coffeehub.brew.domain.RecipeDetailEntity;
import com.ali.coffeehub.brew.service.dto.RecipeDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecipeDetailEntity} and its DTO {@link RecipeDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeDetailMapper extends EntityMapper<RecipeDetailDTO, RecipeDetailEntity> {}
