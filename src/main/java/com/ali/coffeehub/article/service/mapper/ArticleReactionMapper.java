package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.ArticleReactionEntity;
import com.ali.coffeehub.article.service.dto.ArticleReactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleReactionEntity} and its DTO {@link ArticleReactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleReactionMapper extends EntityMapper<ArticleReactionDTO, ArticleReactionEntity> {}
