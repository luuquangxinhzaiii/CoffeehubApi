package com.ali.coffeehub.service.mapper;

import com.ali.coffeehub.domain.CategoryEntity;
import com.ali.coffeehub.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoryEntity} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, CategoryEntity> {}
