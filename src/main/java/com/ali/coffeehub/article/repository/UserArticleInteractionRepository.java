package com.ali.coffeehub.article.repository;

import com.ali.coffeehub.article.domain.UserArticleInteractionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserArticleInteractionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserArticleInteractionRepository extends JpaRepository<UserArticleInteractionEntity, Long> {}
