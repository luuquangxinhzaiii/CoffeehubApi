package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.ArticleStatisticEntity;
import com.ali.coffeehub.article.service.dto.ArticleStatisticDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleStatisticEntity} and its DTO {@link ArticleStatisticDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleStatisticMapper extends EntityMapper<ArticleStatisticDTO, ArticleStatisticEntity> {}
