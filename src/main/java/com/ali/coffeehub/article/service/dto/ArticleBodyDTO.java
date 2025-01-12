package com.ali.coffeehub.article.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.article.domain.ArticleBodyEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleBodyDTO implements Serializable {

    private Long id;

    @Lob
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleBodyDTO)) {
            return false;
        }

        ArticleBodyDTO articleBodyDTO = (ArticleBodyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleBodyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleBodyDTO{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            "}";
    }
}
