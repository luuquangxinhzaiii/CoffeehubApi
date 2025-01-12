package com.ali.coffeehub.cafeteria.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopLocationEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoffeeShopLocationDTO implements Serializable {

    private Long id;

    @NotNull
    private Long coffeeShopId;

    @NotNull
    private String address;

    private Boolean isMainBranch;

    private Boolean deleted;

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

    public Long getCoffeeShopId() {
        return coffeeShopId;
    }

    public void setCoffeeShopId(Long coffeeShopId) {
        this.coffeeShopId = coffeeShopId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsMainBranch() {
        return isMainBranch;
    }

    public void setIsMainBranch(Boolean isMainBranch) {
        this.isMainBranch = isMainBranch;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
        if (!(o instanceof CoffeeShopLocationDTO)) {
            return false;
        }

        CoffeeShopLocationDTO coffeeShopLocationDTO = (CoffeeShopLocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coffeeShopLocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoffeeShopLocationDTO{" +
            "id=" + getId() +
            ", coffeeShopId=" + getCoffeeShopId() +
            ", address='" + getAddress() + "'" +
            ", isMainBranch='" + getIsMainBranch() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
