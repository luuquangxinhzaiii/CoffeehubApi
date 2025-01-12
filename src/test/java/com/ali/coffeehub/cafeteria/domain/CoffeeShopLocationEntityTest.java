package com.ali.coffeehub.cafeteria.domain;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopLocationEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopLocationEntity.class);
        CoffeeShopLocationEntity coffeeShopLocationEntity1 = getCoffeeShopLocationEntitySample1();
        CoffeeShopLocationEntity coffeeShopLocationEntity2 = new CoffeeShopLocationEntity();
        assertThat(coffeeShopLocationEntity1).isNotEqualTo(coffeeShopLocationEntity2);

        coffeeShopLocationEntity2.setId(coffeeShopLocationEntity1.getId());
        assertThat(coffeeShopLocationEntity1).isEqualTo(coffeeShopLocationEntity2);

        coffeeShopLocationEntity2 = getCoffeeShopLocationEntitySample2();
        assertThat(coffeeShopLocationEntity1).isNotEqualTo(coffeeShopLocationEntity2);
    }
}
