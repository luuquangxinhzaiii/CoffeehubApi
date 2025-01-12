package com.ali.coffeehub.repository;

import com.ali.coffeehub.domain.EntityTagsEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntityTagsEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityTagsRepository extends JpaRepository<EntityTagsEntity, Long> {}
