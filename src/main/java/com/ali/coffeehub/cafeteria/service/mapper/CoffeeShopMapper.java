package com.ali.coffeehub.cafeteria.service.mapper;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoffeeShopEntity} and its DTO {@link CoffeeShopDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoffeeShopMapper extends EntityMapper<CoffeeShopDTO, CoffeeShopEntity> {}
