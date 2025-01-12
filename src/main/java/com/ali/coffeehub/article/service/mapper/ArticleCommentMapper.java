package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.ArticleCommentEntity;
import com.ali.coffeehub.article.service.dto.ArticleCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArticleCommentEntity} and its DTO {@link ArticleCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleCommentMapper extends EntityMapper<ArticleCommentDTO, ArticleCommentEntity> {}
