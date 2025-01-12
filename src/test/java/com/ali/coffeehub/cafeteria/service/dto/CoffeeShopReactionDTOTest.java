package com.ali.coffeehub.cafeteria.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopReactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopReactionDTO.class);
        CoffeeShopReactionDTO coffeeShopReactionDTO1 = new CoffeeShopReactionDTO();
        coffeeShopReactionDTO1.setId(1L);
        CoffeeShopReactionDTO coffeeShopReactionDTO2 = new CoffeeShopReactionDTO();
        assertThat(coffeeShopReactionDTO1).isNotEqualTo(coffeeShopReactionDTO2);
        coffeeShopReactionDTO2.setId(coffeeShopReactionDTO1.getId());
        assertThat(coffeeShopReactionDTO1).isEqualTo(coffeeShopReactionDTO2);
        coffeeShopReactionDTO2.setId(2L);
        assertThat(coffeeShopReactionDTO1).isNotEqualTo(coffeeShopReactionDTO2);
        coffeeShopReactionDTO1.setId(null);
        assertThat(coffeeShopReactionDTO1).isNotEqualTo(coffeeShopReactionDTO2);
    }
}
