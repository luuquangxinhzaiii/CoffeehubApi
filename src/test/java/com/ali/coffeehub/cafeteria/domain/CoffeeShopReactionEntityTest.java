package com.ali.coffeehub.cafeteria.domain;

import static com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopReactionEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopReactionEntity.class);
        CoffeeShopReactionEntity coffeeShopReactionEntity1 = getCoffeeShopReactionEntitySample1();
        CoffeeShopReactionEntity coffeeShopReactionEntity2 = new CoffeeShopReactionEntity();
        assertThat(coffeeShopReactionEntity1).isNotEqualTo(coffeeShopReactionEntity2);

        coffeeShopReactionEntity2.setId(coffeeShopReactionEntity1.getId());
        assertThat(coffeeShopReactionEntity1).isEqualTo(coffeeShopReactionEntity2);

        coffeeShopReactionEntity2 = getCoffeeShopReactionEntitySample2();
        assertThat(coffeeShopReactionEntity1).isNotEqualTo(coffeeShopReactionEntity2);
    }
}
