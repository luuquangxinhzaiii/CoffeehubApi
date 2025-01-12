package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.ArticleBodyEntity;
import com.ali.coffeehub.article.service.dto.ArticleBodyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleBodyEntity} and its DTO {@link ArticleBodyDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleBodyMapper extends EntityMapper<ArticleBodyDTO, ArticleBodyEntity> {}
