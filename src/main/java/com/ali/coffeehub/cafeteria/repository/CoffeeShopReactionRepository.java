package com.ali.coffeehub.cafeteria.repository;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoffeeShopReactionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoffeeShopReactionRepository extends JpaRepository<CoffeeShopReactionEntity, Long> {}
