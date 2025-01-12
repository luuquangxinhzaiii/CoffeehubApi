package com.ali.coffeehub.article.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.article.domain.ArticleStatisticEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleStatisticDTO implements Serializable {

    private Long id;

    @NotNull
    private Long aticleId;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private Integer avgTimeSpent;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getAvgTimeSpent() {
        return avgTimeSpent;
    }

    public void setAvgTimeSpent(Integer avgTimeSpent) {
        this.avgTimeSpent = avgTimeSpent;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleStatisticDTO)) {
            return false;
        }

        ArticleStatisticDTO articleStatisticDTO = (ArticleStatisticDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleStatisticDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleStatisticDTO{" +
            "id=" + getId() +
            ", aticleId=" + getAticleId() +
            ", viewCount=" + getViewCount() +
            ", likeCount=" + getLikeCount() +
            ", commentCount=" + getCommentCount() +
            ", avgTimeSpent=" + getAvgTimeSpent() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
