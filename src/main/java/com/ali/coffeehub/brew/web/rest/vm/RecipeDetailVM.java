package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

public class RecipeDetailVM {
    private Long id;

    @NotNull
    @Schema(description = "recipe Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long recipeId;

    @Schema(description = "recipe detail")
    @Lob
    private String detail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NotNull Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
