package com.ali.coffeehub.roaster.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoasterSocialLinkDTO implements Serializable {

    private Long id;

    @NotNull
    private Long roasterId;

    @NotNull
    private Long platform;

    private String url;

    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoasterId() {
        return roasterId;
    }

    public void setRoasterId(Long roasterId) {
        this.roasterId = roasterId;
    }

    public Long getPlatform() {
        return platform;
    }

    public void setPlatform(Long platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoasterSocialLinkDTO)) {
            return false;
        }

        RoasterSocialLinkDTO roasterSocialLinkDTO = (RoasterSocialLinkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roasterSocialLinkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoasterSocialLinkDTO{" +
            "id=" + getId() +
            ", roasterId=" + getRoasterId() +
            ", platform=" + getPlatform() +
            ", url='" + getUrl() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
