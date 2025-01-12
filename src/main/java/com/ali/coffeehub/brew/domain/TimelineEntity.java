package com.ali.coffeehub.brew.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimelineEntity.
 */
@Entity
@Table(name = "timeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimelineEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "config_id", nullable = false)
    private Long configId;

    @NotNull
    @Column(name = "config_value_id", nullable = false)
    private Long configValueId;

    @NotNull
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Integer startTime;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "ratio")
    private Integer ratio;

    @Column(name = "is_fixed")
    private Boolean isFixed;

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

    public TimelineEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return this.configId;
    }

    public TimelineEntity configId(Long configId) {
        this.setConfigId(configId);
        return this;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getConfigValueId() {
        return this.configValueId;
    }

    public TimelineEntity configValueId(Long configValueId) {
        this.setConfigValueId(configValueId);
        return this;
    }

    public void setConfigValueId(Long configValueId) {
        this.configValueId = configValueId;
    }

    public Long getRecipeId() {
        return this.recipeId;
    }

    public TimelineEntity recipeId(Long recipeId) {
        this.setRecipeId(recipeId);
        return this;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getStartTime() {
        return this.startTime;
    }

    public TimelineEntity startTime(Integer startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TimelineEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRatio() {
        return this.ratio;
    }

    public TimelineEntity ratio(Integer ratio) {
        this.setRatio(ratio);
        return this;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public Boolean getIsFixed() {
        return this.isFixed;
    }

    public TimelineEntity isFixed(Boolean isFixed) {
        this.setIsFixed(isFixed);
        return this;
    }

    public void setIsFixed(Boolean isFixed) {
        this.isFixed = isFixed;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public TimelineEntity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TimelineEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public TimelineEntity updatedBy(String updatedBy) {
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
        if (!(o instanceof TimelineEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((TimelineEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimelineEntity{" +
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
