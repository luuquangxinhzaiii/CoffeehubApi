package com.ali.coffeehub.cafeteria.service.mapper;

import com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity;
import com.ali.coffeehub.cafeteria.service.dto.CoffeeShopReactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoffeeShopReactionEntity} and its DTO {@link CoffeeShopReactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoffeeShopReactionMapper extends EntityMapper<CoffeeShopReactionDTO, CoffeeShopReactionEntity> {}
