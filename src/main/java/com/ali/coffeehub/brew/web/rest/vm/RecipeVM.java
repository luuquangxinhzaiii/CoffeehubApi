package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class RecipeVM implements Serializable {
    private Long id;

    @NotNull
    @Schema(description = "Brew Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long brewId;

    @NotNull
    @Schema(description = "Recipe method name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Recipe method description")
    private String detail;

    @Schema(description = "Recipe detail")
    private RecipeDetailVM recipeDetailVM;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getBrewId() {
        return brewId;
    }

    public void setBrewId(@NotNull Long brewId) {
        this.brewId = brewId;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public RecipeDetailVM getRecipeDetailVM() {
        return recipeDetailVM;
    }

    public void setRecipeDetailVM(RecipeDetailVM recipeDetailVM) {
        this.recipeDetailVM = recipeDetailVM;
    }
}
