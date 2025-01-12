package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.ConfigValueEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfigValueEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigValueRepository extends JpaRepository<ConfigValueEntity, Long> {}
