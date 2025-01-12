package com.ali.coffeehub.repository;

import com.ali.coffeehub.domain.CategoryEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoryEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {}
