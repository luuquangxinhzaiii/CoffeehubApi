package com.ali.coffeehub.cafeteria.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoffeeShopEntity.
 */
@Entity
@Table(name = "coffee_shop")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoffeeShopEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "logo_uri", nullable = false)
    private String logoUri;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "phone")
    private String phone;

    @Column(name = "available_hours_start")
    private Instant availableHoursStart;

    @Column(name = "available_hours_end")
    private Instant availableHoursEnd;

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

    public CoffeeShopEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public CoffeeShopEntity categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return this.name;
    }

    public CoffeeShopEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUri() {
        return this.logoUri;
    }

    public CoffeeShopEntity logoUri(String logoUri) {
        this.setLogoUri(logoUri);
        return this;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public CoffeeShopEntity deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPhone() {
        return this.phone;
    }

    public CoffeeShopEntity phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getAvailableHoursStart() {
        return this.availableHoursStart;
    }

    public CoffeeShopEntity availableHoursStart(Instant availableHoursStart) {
        this.setAvailableHoursStart(availableHoursStart);
        return this;
    }

    public void setAvailableHoursStart(Instant availableHoursStart) {
        this.availableHoursStart = availableHoursStart;
    }

    public Instant getAvailableHoursEnd() {
        return this.availableHoursEnd;
    }

    public CoffeeShopEntity availableHoursEnd(Instant availableHoursEnd) {
        this.setAvailableHoursEnd(availableHoursEnd);
        return this;
    }

    public void setAvailableHoursEnd(Instant availableHoursEnd) {
        this.availableHoursEnd = availableHoursEnd;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CoffeeShopEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CoffeeShopEntity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CoffeeShopEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public CoffeeShopEntity updatedBy(String updatedBy) {
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
        if (!(o instanceof CoffeeShopEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((CoffeeShopEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoffeeShopEntity{" +
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
