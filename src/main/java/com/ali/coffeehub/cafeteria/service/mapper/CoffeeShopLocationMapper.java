package com.ali.coffeehub.cafeteria.service.mapper;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoffeeShopLocationEntity} and its DTO {@link CoffeeShopLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoffeeShopLocationMapper extends EntityMapper<CoffeeShopLocationDTO, CoffeeShopLocationEntity> {}
