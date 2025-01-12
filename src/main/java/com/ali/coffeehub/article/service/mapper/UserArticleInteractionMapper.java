package com.ali.coffeehub.article.service.mapper;

import com.ali.coffeehub.article.domain.UserArticleInteractionEntity;
import com.ali.coffeehub.article.service.dto.UserArticleInteractionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserArticleInteractionEntity} and its DTO {@link UserArticleInteractionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserArticleInteractionMapper extends EntityMapper<UserArticleInteractionDTO, UserArticleInteractionEntity> {}
