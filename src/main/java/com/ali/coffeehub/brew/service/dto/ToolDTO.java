package com.ali.coffeehub.brew.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.brew.domain.ToolEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToolDTO implements Serializable {

    private Long id;

    @NotNull
    private Long brewId;

    @NotNull
    private String name;

    @Lob
    private String detail;

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

    public Long getBrewId() {
        return brewId;
    }

    public void setBrewId(Long brewId) {
        this.brewId = brewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        if (!(o instanceof ToolDTO)) {
            return false;
        }

        ToolDTO toolDTO = (ToolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolDTO{" +
            "id=" + getId() +
            ", brewId=" + getBrewId() +
            ", name='" + getName() + "'" +
            ", detail='" + getDetail() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
