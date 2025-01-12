package com.ali.coffeehub.cafeteria.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoffeeShopDTO implements Serializable {

    private Long id;

    @NotNull
    private Long categoryId;

    @NotNull
    private String name;

    @NotNull
    private String logoUri;

    private Boolean deleted;

    private String phone;

    private Instant availableHoursStart;

    private Instant availableHoursEnd;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getAvailableHoursStart() {
        return availableHoursStart;
    }

    public void setAvailableHoursStart(Instant availableHoursStart) {
        this.availableHoursStart = availableHoursStart;
    }

    public Instant getAvailableHoursEnd() {
        return availableHoursEnd;
    }

    public void setAvailableHoursEnd(Instant availableHoursEnd) {
        this.availableHoursEnd = availableHoursEnd;
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
        if (!(o instanceof CoffeeShopDTO)) {
            return false;
        }

        CoffeeShopDTO coffeeShopDTO = (CoffeeShopDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coffeeShopDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoffeeShopDTO{" +
            "id=" + getId() +
            ", categoryId=" + getCategoryId() +
            ", name='" + getName() + "'" +
            ", logoUri='" + getLogoUri() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", phone='" + getPhone() + "'" +
            ", availableHoursStart='" + getAvailableHoursStart() + "'" +
            ", availableHoursEnd='" + getAvailableHoursEnd() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
