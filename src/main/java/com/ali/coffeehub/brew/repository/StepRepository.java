package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.StepEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StepEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepRepository extends JpaRepository<StepEntity, Long> {}
