package com.ali.coffeehub.roaster.repository;

import com.ali.coffeehub.roaster.domain.RoasterEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoasterEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoasterRepository extends JpaRepository<RoasterEntity, Long> {}
