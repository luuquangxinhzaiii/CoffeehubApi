package com.ali.coffeehub.brew.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.brew.domain.TimelineEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimelineDTO implements Serializable {

    private Long id;

    @NotNull
    private Long configId;

    @NotNull
    private Long configValueId;

    @NotNull
    private Long recipeId;

    @NotNull
    private Integer startTime;

    @NotNull
    private Instant createdAt;

    private Integer ratio;

    private Boolean isFixed;

    private String createdBy;

    private Instant updatedAt;

    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getConfigValueId() {
        return configValueId;
    }

    public void setConfigValueId(Long configValueId) {
        this.configValueId = configValueId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public Boolean getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        this.isFixed = isFixed;
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
        if (!(o instanceof TimelineDTO)) {
            return false;
        }

        TimelineDTO timelineDTO = (TimelineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timelineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimelineDTO{" +
            "id=" + getId() +
            ", configId=" + getConfigId() +
            ", configValueId=" + getConfigValueId() +
            ", recipeId=" + getRecipeId() +
            ", startTime=" + getStartTime() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", ratio=" + getRatio() +
            ", isFixed='" + getIsFixed() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
