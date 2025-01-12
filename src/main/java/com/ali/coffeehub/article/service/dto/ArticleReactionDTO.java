package com.ali.coffeehub.article.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.article.domain.ArticleReactionEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleReactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long aticleId;

    @NotNull
    private Long userId;

    @NotNull
    private Instant createdAt;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleReactionDTO)) {
            return false;
        }

        ArticleReactionDTO articleReactionDTO = (ArticleReactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleReactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleReactionDTO{" +
            "id=" + getId() +
            ", aticleId=" + getAticleId() +
            ", userId=" + getUserId() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
