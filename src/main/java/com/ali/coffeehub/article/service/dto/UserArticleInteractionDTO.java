package com.ali.coffeehub.article.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.article.domain.UserArticleInteractionEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserArticleInteractionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long aticleId;

    @NotNull
    private Long userId;

    private Integer readProgress;

    private Boolean isBookmarked;

    private Instant lastReadAt;

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

    public Long getAticleId() {
        return aticleId;
    }

    public void setAticleId(Long aticleId) {
        this.aticleId = aticleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getReadProgress() {
        return readProgress;
    }

    public void setReadProgress(Integer readProgress) {
        this.readProgress = readProgress;
    }

    public Boolean getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public Instant getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
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
        if (!(o instanceof UserArticleInteractionDTO)) {
            return false;
        }

        UserArticleInteractionDTO userArticleInteractionDTO = (UserArticleInteractionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userArticleInteractionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserArticleInteractionDTO{" +
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
