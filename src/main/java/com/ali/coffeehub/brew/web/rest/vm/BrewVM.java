package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

public class BrewVM implements Serializable {
    @NotNull
    @Schema(description = "Category id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotNull
    @Schema(description = "Brew method name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Brew method description")
    private String description;

    @Schema(description = "level")
    private Integer level;

    @Schema(description = "Serving type")
    private String serving;

    @Schema(description = "icon uri")
    private String iconUri;

    @Schema(description = "image uri")
    private String imageUri;

    @Schema(description = "pin status")
    private Boolean isPinned;

    @NotNull
    @Schema(description = "recipes")
    Set<RecipeVM> recipes;

    @NotNull
    @Schema(description = "steps")
    Set<StepVM> steps;

    @NotNull
    @Schema(description = "tools")
    Set<ToolVM> tool;

    public @NotNull Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull Long categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getPinned() {
        return isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
    }

    public @NotNull Set<RecipeVM> getRecipes() {
        return recipes;
    }

    public void setRecipes(@NotNull Set<RecipeVM> recipes) {
        this.recipes = recipes;
    }

    public @NotNull Set<StepVM> getSteps() {
        return steps;
    }

    public void setSteps(@NotNull Set<StepVM> steps) {
        this.steps = steps;
    }

    public @NotNull Set<ToolVM> getTool() {
        return tool;
    }

    public void setTool(@NotNull Set<ToolVM> tool) {
        this.tool = tool;
    }
}
