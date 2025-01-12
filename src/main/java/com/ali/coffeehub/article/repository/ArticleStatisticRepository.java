package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.ArticleStatisticEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleStatisticEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleStatisticRepository extends JpaRepository<ArticleStatisticEntity, Long> {}
