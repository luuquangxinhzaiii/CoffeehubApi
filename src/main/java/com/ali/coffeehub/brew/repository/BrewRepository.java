package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.BrewEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BrewEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrewRepository extends JpaRepository<BrewEntity, Long> {}
