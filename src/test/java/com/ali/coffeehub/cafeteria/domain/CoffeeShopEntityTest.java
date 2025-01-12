package com.ali.coffeehub.cafeteria.domain;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopEntity.class);
        CoffeeShopEntity coffeeShopEntity1 = getCoffeeShopEntitySample1();
        CoffeeShopEntity coffeeShopEntity2 = new CoffeeShopEntity();
        assertThat(coffeeShopEntity1).isNotEqualTo(coffeeShopEntity2);

        coffeeShopEntity2.setId(coffeeShopEntity1.getId());
        assertThat(coffeeShopEntity1).isEqualTo(coffeeShopEntity2);

        coffeeShopEntity2 = getCoffeeShopEntitySample2();
        assertThat(coffeeShopEntity1).isNotEqualTo(coffeeShopEntity2);
    }
}
