package com.ali.coffeehub.brew.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ToolVM implements Serializable {
    private Long id;

    @NotNull
    @Schema(description = "Brew Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long brewId;

    @NotNull
    @Schema(description = "Brew tool name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Brew tool detail")
    @Lob
    private String detail;

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
}
