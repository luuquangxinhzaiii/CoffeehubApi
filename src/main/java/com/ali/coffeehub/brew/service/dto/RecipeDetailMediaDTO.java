package com.ali.coffeehub.brew.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.brew.domain.RecipeDetailMediaEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipeDetailMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long mediaId;

    @NotNull
    private Long recipeDetailId;

    @NotNull
    private Instant createdAt;

    private String createdBy;

    private Instant updatedAt;

    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Long getRecipeDetailId() {
        return recipeDetailId;
    }

    public void setRecipeDetailId(Long recipeDetailId) {
        this.recipeDetailId = recipeDetailId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipeDetailMediaDTO)) {
            return false;
        }

        RecipeDetailMediaDTO recipeDetailMediaDTO = (RecipeDetailMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recipeDetailMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipeDetailMediaDTO{" +
            "id=" + getId() +
            ", mediaId=" + getMediaId() +
            ", recipeDetailId=" + getRecipeDetailId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
