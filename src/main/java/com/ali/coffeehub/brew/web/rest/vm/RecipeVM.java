package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class RecipeVM implements Serializable {
    @NotNull
    @Schema(description = "Recipe method name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Recipe method description")
    private String detail;
}
