package com.ali.coffeehub.cafeteria.repository;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoffeeShopEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoffeeShopRepository extends JpaRepository<CoffeeShopEntity, Long> {}
