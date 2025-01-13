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

    Set<RecipeVM> recipes;

    Set<StepVM> steps;


}
