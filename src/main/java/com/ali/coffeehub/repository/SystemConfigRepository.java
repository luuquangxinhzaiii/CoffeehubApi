package com.ali.coffeehub.repository;

import com.ali.coffeehub.domain.SystemConfigEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemConfigEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, Long> {}
