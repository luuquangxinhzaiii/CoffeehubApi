package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.ArticleCommentEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleCommentEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, Long> {}
