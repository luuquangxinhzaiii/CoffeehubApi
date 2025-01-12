package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.RecipeEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecipeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {}
