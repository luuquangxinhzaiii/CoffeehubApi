package com.ali.coffeehub.roaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RoasterSocialLinkEntity.
 */
@Entity
@Table(name = "roaster_social_link")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoasterSocialLinkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "roaster_id", nullable = false)
    private Long roasterId;

    @NotNull
    @Column(name = "platform", nullable = false)
    private Long platform;

    @Column(name = "url")
    private String url;

    @Column(name = "deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoasterSocialLinkEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoasterId() {
        return this.roasterId;
    }

    public RoasterSocialLinkEntity roasterId(Long roasterId) {
        this.setRoasterId(roasterId);
        return this;
    }

    public void setRoasterId(Long roasterId) {
        this.roasterId = roasterId;
    }

    public Long getPlatform() {
        return this.platform;
    }

    public RoasterSocialLinkEntity platform(Long platform) {
        this.setPlatform(platform);
        return this;
    }

    public void setPlatform(Long platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return this.url;
    }

    public RoasterSocialLinkEntity url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public RoasterSocialLinkEntity deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoasterSocialLinkEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((RoasterSocialLinkEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoasterSocialLinkEntity{" +
            "id=" + getId() +
            ", roasterId=" + getRoasterId() +
            ", platform=" + getPlatform() +
            ", url='" + getUrl() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
