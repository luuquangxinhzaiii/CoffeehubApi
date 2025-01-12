package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.TimelineEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimelineEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimelineRepository extends JpaRepository<TimelineEntity, Long> {}
