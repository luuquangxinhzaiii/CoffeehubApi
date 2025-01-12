package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.ArticleBodyEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleBodyEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleBodyRepository extends JpaRepository<ArticleBodyEntity, Long> {}
