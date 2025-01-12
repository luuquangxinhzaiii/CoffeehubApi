package com.ali.coffeehub.cafeteria.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.cafeteria.domain.CoffeeShopReactionEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoffeeShopReactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long coffeeShopId;

    @NotNull
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        if (!(o instanceof CoffeeShopReactionDTO)) {
            return false;
        }

        CoffeeShopReactionDTO coffeeShopReactionDTO = (CoffeeShopReactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coffeeShopReactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoffeeShopReactionDTO{" +
            "id=" + getId() +
            ", coffeeShopId=" + getCoffeeShopId() +
            ", userId=" + getUserId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
