package com.ali.coffeehub.repository;

import com.ali.coffeehub.domain.MediaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MediaEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {}
