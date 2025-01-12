package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecipeDetailMediaEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeDetailMediaRepository extends JpaRepository<RecipeDetailMediaEntity, Long> {}
