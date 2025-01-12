package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.ToolEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ToolEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {}
