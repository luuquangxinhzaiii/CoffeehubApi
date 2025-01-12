package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.ArticleReactionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleReactionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleReactionRepository extends JpaRepository<ArticleReactionEntity, Long> {}
