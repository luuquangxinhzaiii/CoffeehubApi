package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RecipeEntity getRecipeEntitySample1() {
        return new RecipeEntity().id(1L).brewId(1L).name("name1").detail("detail1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static RecipeEntity getRecipeEntitySample2() {
        return new RecipeEntity().id(2L).brewId(2L).name("name2").detail("detail2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static RecipeEntity getRecipeEntityRandomSampleGenerator() {
        return new RecipeEntity()
            .id(longCount.incrementAndGet())
            .brewId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .detail(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
