package com.ali.coffeehub.article.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArticlesEntity.
 */
@Entity
@Table(name = "articles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticlesEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long categoryID;

    @NotNull
    @Column(name = "body_id", nullable = false)
    private Long bodyId;

    @NotNull
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "reading_time")
    private Integer readingTime;

    @Column(name = "is_pinned")
    private Boolean isPinned;

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

    public ArticlesEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryID() {
        return this.categoryID;
    }

    public ArticlesEntity categoryID(Long categoryID) {
        this.setCategoryID(categoryID);
        return this;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public Long getBodyId() {
        return this.bodyId;
    }

    public ArticlesEntity bodyId(Long bodyId) {
        this.setBodyId(bodyId);
        return this;
    }

    public void setBodyId(Long bodyId) {
        this.bodyId = bodyId;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public ArticlesEntity authorId(Long authorId) {
        this.setAuthorId(authorId);
        return this;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return this.title;
    }

    public ArticlesEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public ArticlesEntity slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public ArticlesEntity subTitle(String subTitle) {
        this.setSubTitle(subTitle);
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public ArticlesEntity thumbnailUrl(String thumbnailUrl) {
        this.setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getReadingTime() {
        return this.readingTime;
    }

    public ArticlesEntity readingTime(Integer readingTime) {
        this.setReadingTime(readingTime);
        return this;
    }

    public void setReadingTime(Integer readingTime) {
        this.readingTime = readingTime;
    }

    public Boolean getIsPinned() {
        return this.isPinned;
    }

    public ArticlesEntity isPinned(Boolean isPinned) {
        this.setIsPinned(isPinned);
        return this;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public ArticlesEntity deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ArticlesEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ArticlesEntity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ArticlesEntity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ArticlesEntity updatedBy(String updatedBy) {
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
        if (!(o instanceof ArticlesEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ArticlesEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticlesEntity{" +
            "id=" + getId() +
            ", categoryID=" + getCategoryID() +
            ", bodyId=" + getBodyId() +
            ", authorId=" + getAuthorId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", thumbnailUrl='" + getThumbnailUrl() + "'" +
            ", readingTime=" + getReadingTime() +
            ", isPinned='" + getIsPinned() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
