package com.ali.coffeehub.brew.service.dto;

import com.ali.coffeehub.brew.web.rest.vm.RecipeVM;
import com.ali.coffeehub.brew.web.rest.vm.StepVM;
import com.ali.coffeehub.brew.web.rest.vm.ToolVM;
import com.ali.coffeehub.service.dto.AbstractAuditingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.ali.coffeehub.brew.domain.BrewEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BrewDTO extends AbstractAuditingDTO implements Serializable {

    private final Long id;

    @NotNull
    @Schema(description = "Category id", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Long categoryId;

    @NotNull
    @Schema(description = "brew name", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String name;

    @Schema(description = "Brew method description")
    private final String description;

    @Schema(description = "level")
    private final Integer level;

    @Schema(description = "Serving type")
    private final String serving;

    @Schema(description = "icon uri")
    private final String iconUri;

    @Schema(description = "image uri")
    private final String imageUri;

    @NotNull
    @Schema(description = "Soft delete", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Boolean deleted;

    @Schema(description = "pin status")
    private final Boolean isPinned;

    @NotNull
    @Schema(description = "recipes")
    private final Set<RecipeDTO> recipes;

    @NotNull
    @Schema(description = "steps")
    private final Set<StepDTO> steps;

    @NotNull
    @Schema(description = "tools")
    private final Set<ToolDTO> tool;

    public Long getId() {
        return id;
    }

    public @NotNull Long getCategoryId() {
        return categoryId;
    }

    public @NotNull String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public String getServing() {
        return serving;
    }

    public String getIconUri() {
        return iconUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public @NotNull Boolean getDeleted() {
        return deleted;
    }

    public Boolean getPinned() {
        return isPinned;
    }

    public @NotNull Set<RecipeDTO> getRecipes() {
        return recipes;
    }

    public @NotNull Set<StepDTO> getSteps() {
        return steps;
    }

    public @NotNull Set<ToolDTO> getTool() {
        return tool;
    }

    public BrewDTO(Builder builder){
        this.id = builder.getId();
        this.categoryId = builder.categoryId;
        this.name = builder.name;
        this.description = builder.description;
        this.level = builder.level;
        this.serving = builder.serving;
        this.iconUri = builder.iconUri;
        this.imageUri = builder.imageUri;
        this.deleted = builder.deleted;
        this.isPinned = builder.isPinned;
        this.recipes = Optional.ofNullable(builder.getRecipes())
            .orElseGet(Collections::emptySet)
            .stream()
            .filter(Objects::nonNull)
            .map(RecipeDTO.Builder::build)
            .collect(Collectors.toSet());
        this.steps = Optional.ofNullable(builder.getSteps())
            .orElseGet(Collections::emptySet)
            .stream()
            .filter(Objects::nonNull)
            .map(StepDTO.Builder::build)
            .collect(Collectors.toSet());
        this.tool = Optional.ofNullable(builder.getTool())
            .orElseGet(Collections::emptySet)
            .stream()
            .filter(Objects::nonNull)
            .map(ToolDTO.Builder::build)
            .collect(Collectors.toSet());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long categoryId;
        private String name;
        private String description;
        private Integer level;
        private String serving;
        private String iconUri;
        private String imageUri;
        private Boolean deleted;
        private Boolean isPinned;
        private Set<RecipeDTO.Builder> recipes;
        private Set<StepDTO.Builder> steps;
        private Set<ToolDTO.Builder> tool;


        public Long getId() {
            return id;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Integer getLevel() {
            return level;
        }

        public String getServing() {
            return serving;
        }

        public String getIconUri() {
            return iconUri;
        }

        public String getImageUri() {
            return imageUri;
        }

        public Boolean getDeleted() {
            return deleted;
        }

        public Boolean getPinned() {
            return isPinned;
        }

        public Set<RecipeDTO.Builder> getRecipes() {
            return recipes;
        }

        public Set<StepDTO.Builder> getSteps() {
            return steps;
        }

        public Set<ToolDTO.Builder> getTool() {
            return tool;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder serving(String serving) {
            this.serving = serving;
            return this;
        }

        public Builder iconUri(String iconUri) {
            this.iconUri = iconUri;
            return this;
        }

        public Builder imageUri(String imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public Builder deleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Builder isPinned(Boolean isPinned) {
            this.isPinned = isPinned;
            return this;
        }

        public Builder recipes(Set<RecipeDTO.Builder> recipes) {
            this.recipes = recipes;
            return this;
        }

        public Builder steps(Set<StepDTO.Builder> steps) {
            this.steps = steps;
            return this;
        }

        public Builder tool(Set<ToolDTO.Builder> tool) {
            this.tool = tool;
            return this;
        }

        public BrewDTO build() {
            return new BrewDTO(this);
        }
    }
}
