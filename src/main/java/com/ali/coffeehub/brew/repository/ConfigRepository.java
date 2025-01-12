package com.ali.coffeehub.brew.repository;

import com.ali.coffeehub.brew.domain.ConfigEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfigEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {}
