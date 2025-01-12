package com.ali.coffeehub.repository;

import com.ali.coffeehub.domain.TagsEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TagsEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagsRepository extends JpaRepository<TagsEntity, Long> {}
