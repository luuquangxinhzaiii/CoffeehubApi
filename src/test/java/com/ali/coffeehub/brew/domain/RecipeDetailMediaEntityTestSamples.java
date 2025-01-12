package com.ali.coffeehub.brew.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeDetailMediaEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RecipeDetailMediaEntity getRecipeDetailMediaEntitySample1() {
        return new RecipeDetailMediaEntity().id(1L).mediaId(1L).recipeDetailId(1L).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static RecipeDetailMediaEntity getRecipeDetailMediaEntitySample2() {
        return new RecipeDetailMediaEntity().id(2L).mediaId(2L).recipeDetailId(2L).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static RecipeDetailMediaEntity getRecipeDetailMediaEntityRandomSampleGenerator() {
        return new RecipeDetailMediaEntity()
            .id(longCount.incrementAndGet())
            .mediaId(longCount.incrementAndGet())
            .recipeDetailId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
