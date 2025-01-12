package com.ali.coffeehub.cafeteria.service.mapper;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopEntityAsserts.*;
import static com.ali.coffeehub.cafeteria.domain.CoffeeShopEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeShopMapperTest {

    private CoffeeShopMapper coffeeShopMapper;

    @BeforeEach
    void setUp() {
        coffeeShopMapper = new CoffeeShopMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCoffeeShopEntitySample1();
        var actual = coffeeShopMapper.toEntity(coffeeShopMapper.toDto(expected));
        assertCoffeeShopEntityAllPropertiesEquals(expected, actual);
    }
}
