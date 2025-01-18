package com.ali.coffeehub.brew.service.dto;

import com.ali.coffeehub.service.dto.AbstractAuditingDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.brew.domain.RecipeEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipeDTO extends AbstractAuditingDTO implements Serializable {

    private final Long id;

    @NotNull
    private final Long brewId;

    private final String name;

    private final String detail;

    public Long getId() {
        return id;
    }

    public @NotNull Long getBrewId() {
        return brewId;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }


    private RecipeDTO(Builder builder) {
        this.id = builder.getId();
        this.brewId = builder.getBrewId();
        this.name = builder.getName();
        this.detail = builder.getDetail();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long brewId;
        private String name;
        private String detail;

        public Long getId() {
            return id;
        }

        public Long getBrewId() {
            return brewId;
        }

        public String getName() {
            return name;
        }

        public String getDetail() {
            return detail;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brewId(Long brewId) {
            this.brewId = brewId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public RecipeDTO build() {
            return new RecipeDTO(this);
        }
    }
}
