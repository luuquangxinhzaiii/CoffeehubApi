package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.ArticlesEntity;
import com.ali.coffeehub.article.service.dto.ArticlesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticlesEntity} and its DTO {@link ArticlesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticlesMapper extends EntityMapper<ArticlesDTO, ArticlesEntity> {}
