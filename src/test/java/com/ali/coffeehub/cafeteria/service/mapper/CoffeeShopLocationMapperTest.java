package com.ali.coffeehub.cafeteria.service.mapper;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntityAsserts.*;
import static com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeShopLocationMapperTest {

    private CoffeeShopLocationMapper coffeeShopLocationMapper;

    @BeforeEach
    void setUp() {
        coffeeShopLocationMapper = new CoffeeShopLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCoffeeShopLocationEntitySample1();
        var actual = coffeeShopLocationMapper.toEntity(coffeeShopLocationMapper.toDto(expected));
        assertCoffeeShopLocationEntityAllPropertiesEquals(expected, actual);
    }
}
