package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ToolVM implements Serializable {
    @NotNull
    @Schema(description = "Brew tool name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Brew tool detail")
    @Lob
    private String detail;
}
