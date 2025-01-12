package com.ali.coffeehub.cafeteria.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoffeeShopLocationEntity.
 */
@Entity
@Table(name = "coffee_shop_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoffeeShopLocationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "coffee_shop_id", nullable = false)
    private Long coffeeShopId;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "is_main_branch")
    private Boolean isMainBranch;

    @Column(name = "deleted")
    private Boolean deleted;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CoffeeShopLocationEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoffeeShopId() {
        return this.coffeeShopId;
    }

    public CoffeeShopLocationEntity coffeeShopId(Long coffeeShopId) {
        this.setCoffeeShopId(coffeeShopId);
        return this;
    }

    public void setCoffeeShopId(Long coffeeShopId) {
        this.coffeeShopId = coffeeShopId;
    }

    public String getAddress() {
        return this.address;
    }

    public CoffeeShopLocationEntity address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsMainBranch() {
        return this.isMainBranch;
    }

    public CoffeeShopLocationEntity isMainBranch(Boolean isMainBranch) {
        this.setIsMainBranch(isMainBranch);
        return this;
    }

    public void setIsMainBranch(Boolean isMainBranch) {
        this.isMainBranch = isMainBranch;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public CoffeeShopLocationEntity deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CoffeeShopLocationEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CoffeeShopLocationEntity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CoffeeShopLocationEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public CoffeeShopLocationEntity updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoffeeShopLocationEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((CoffeeShopLocationEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoffeeShopLocationEntity{" +
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
