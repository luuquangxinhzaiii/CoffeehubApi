package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeDetailEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RecipeDetailEntity getRecipeDetailEntitySample1() {
        return new RecipeDetailEntity().id(1L).recipeId(1L).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static RecipeDetailEntity getRecipeDetailEntitySample2() {
        return new RecipeDetailEntity().id(2L).recipeId(2L).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static RecipeDetailEntity getRecipeDetailEntityRandomSampleGenerator() {
        return new RecipeDetailEntity()
            .id(longCount.incrementAndGet())
            .recipeId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
