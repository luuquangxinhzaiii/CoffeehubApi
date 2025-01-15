package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class StepVM implements Serializable {
    @NotNull
    @Schema(description = "Brew step name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Brew step detail")
    @Lob
    private String detail;
}
