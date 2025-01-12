package com.ali.coffeehub.cafeteria.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopLocationDTO.class);
        CoffeeShopLocationDTO coffeeShopLocationDTO1 = new CoffeeShopLocationDTO();
        coffeeShopLocationDTO1.setId(1L);
        CoffeeShopLocationDTO coffeeShopLocationDTO2 = new CoffeeShopLocationDTO();
        assertThat(coffeeShopLocationDTO1).isNotEqualTo(coffeeShopLocationDTO2);
        coffeeShopLocationDTO2.setId(coffeeShopLocationDTO1.getId());
        assertThat(coffeeShopLocationDTO1).isEqualTo(coffeeShopLocationDTO2);
        coffeeShopLocationDTO2.setId(2L);
        assertThat(coffeeShopLocationDTO1).isNotEqualTo(coffeeShopLocationDTO2);
        coffeeShopLocationDTO1.setId(null);
        assertThat(coffeeShopLocationDTO1).isNotEqualTo(coffeeShopLocationDTO2);
    }
}
