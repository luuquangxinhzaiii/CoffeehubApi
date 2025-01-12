package com.ali.coffeehub.article.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserArticleInteractionEntity.
 */
@Entity
@Table(name = "user_article_interaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserArticleInteractionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "aticle_id", nullable = false)
    private Long aticleId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "read_progress")
    private Integer readProgress;

    @Column(name = "is_bookmarked")
    private Boolean isBookmarked;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

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

    public UserArticleInteractionEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAticleId() {
        return this.aticleId;
    }

    public UserArticleInteractionEntity aticleId(Long aticleId) {
        this.setAticleId(aticleId);
        return this;
    }

    public void setAticleId(Long aticleId) {
        this.aticleId = aticleId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserArticleInteractionEntity userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getReadProgress() {
        return this.readProgress;
    }

    public UserArticleInteractionEntity readProgress(Integer readProgress) {
        this.setReadProgress(readProgress);
        return this;
    }

    public void setReadProgress(Integer readProgress) {
        this.readProgress = readProgress;
    }

    public Boolean getIsBookmarked() {
        return this.isBookmarked;
    }

    public UserArticleInteractionEntity isBookmarked(Boolean isBookmarked) {
        this.setIsBookmarked(isBookmarked);
        return this;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public Instant getLastReadAt() {
        return this.lastReadAt;
    }

    public UserArticleInteractionEntity lastReadAt(Instant lastReadAt) {
        this.setLastReadAt(lastReadAt);
        return this;
    }

    public void setLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserArticleInteractionEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserArticleInteractionEntity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserArticleInteractionEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public UserArticleInteractionEntity updatedBy(String updatedBy) {
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
        if (!(o instanceof UserArticleInteractionEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((UserArticleInteractionEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserArticleInteractionEntity{" +
            "id=" + getId() +
            ", aticleId=" + getAticleId() +
            ", userId=" + getUserId() +
            ", readProgress=" + getReadProgress() +
            ", isBookmarked='" + getIsBookmarked() + "'" +
            ", lastReadAt='" + getLastReadAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
