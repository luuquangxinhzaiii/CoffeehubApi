package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.RecipeDetailEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecipeDetailEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeDetailRepository extends JpaRepository<RecipeDetailEntity, Long> {}
