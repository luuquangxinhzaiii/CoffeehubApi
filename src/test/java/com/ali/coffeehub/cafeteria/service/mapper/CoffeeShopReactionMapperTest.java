package com.ali.coffeehub.cafeteria.service.mapper;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntityAsserts.*;
import static com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeShopReactionMapperTest {

    private CoffeeShopReactionMapper coffeeShopReactionMapper;

    @BeforeEach
    void setUp() {
        coffeeShopReactionMapper = new CoffeeShopReactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCoffeeShopReactionEntitySample1();
        var actual = coffeeShopReactionMapper.toEntity(coffeeShopReactionMapper.toDto(expected));
        assertCoffeeShopReactionEntityAllPropertiesEquals(expected, actual);
    }
}
