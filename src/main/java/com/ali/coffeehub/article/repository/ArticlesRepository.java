package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.ArticlesEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticlesEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticlesRepository extends JpaRepository<ArticlesEntity, Long> {}
