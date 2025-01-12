package com.ali.coffeehub.roaster.repository;

import com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoasterSocialLinkEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoasterSocialLinkRepository extends JpaRepository<RoasterSocialLinkEntity, Long> {}
