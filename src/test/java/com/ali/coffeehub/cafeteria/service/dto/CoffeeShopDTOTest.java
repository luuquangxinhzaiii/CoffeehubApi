package com.ali.coffeehub.cafeteria.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ali.coffeehub.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoffeeShopDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeShopDTO.class);
        CoffeeShopDTO coffeeShopDTO1 = new CoffeeShopDTO();
        coffeeShopDTO1.setId(1L);
        CoffeeShopDTO coffeeShopDTO2 = new CoffeeShopDTO();
        assertThat(coffeeShopDTO1).isNotEqualTo(coffeeShopDTO2);
        coffeeShopDTO2.setId(coffeeShopDTO1.getId());
        assertThat(coffeeShopDTO1).isEqualTo(coffeeShopDTO2);
        coffeeShopDTO2.setId(2L);
        assertThat(coffeeShopDTO1).isNotEqualTo(coffeeShopDTO2);
        coffeeShopDTO1.setId(null);
        assertThat(coffeeShopDTO1).isNotEqualTo(coffeeShopDTO2);
    }
}
