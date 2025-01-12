package com.ali.coffeehub.cafeteria.repository;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoffeeShopLocationEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoffeeShopLocationRepository extends JpaRepository<CoffeeShopLocationEntity, Long> {}
