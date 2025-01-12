package com.ali.coffeehub.article.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArticleStatisticEntity.
 */
@Entity
@Table(name = "article_statistic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleStatisticEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "aticle_id", nullable = false)
    private Long aticleId;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "avg_time_spent")
    private Integer avgTimeSpent;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArticleStatisticEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAticleId() {
        return this.aticleId;
    }

    public ArticleStatisticEntity aticleId(Long aticleId) {
        this.setAticleId(aticleId);
        return this;
    }

    public void setAticleId(Long aticleId) {
        this.aticleId = aticleId;
    }

    public Integer getViewCount() {
        return this.viewCount;
    }

    public ArticleStatisticEntity viewCount(Integer viewCount) {
        this.setViewCount(viewCount);
        return this;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public ArticleStatisticEntity likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return this.commentCount;
    }

    public ArticleStatisticEntity commentCount(Integer commentCount) {
        this.setCommentCount(commentCount);
        return this;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getAvgTimeSpent() {
        return this.avgTimeSpent;
    }

    public ArticleStatisticEntity avgTimeSpent(Integer avgTimeSpent) {
        this.setAvgTimeSpent(avgTimeSpent);
        return this;
    }

    public void setAvgTimeSpent(Integer avgTimeSpent) {
        this.avgTimeSpent = avgTimeSpent;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ArticleStatisticEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ArticleStatisticEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleStatisticEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ArticleStatisticEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleStatisticEntity{" +
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
