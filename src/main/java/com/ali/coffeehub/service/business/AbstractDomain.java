package com.ali.coffeehub.service.business;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class AbstractDomain<ID extends Serializable> implements IdentityGetter<ID>, Serializable {

    /**
     * Identity
     */
    private ID id;

    /**
     * Mark as creating
     */
    @JsonIgnore
    private boolean creating;

    /**
     * Mark as updating
     */
    @JsonIgnore
    private boolean updating;

    /**
     *  When create a Domain from DI, use this property to ensure the domain is ready or not
     */
    @JsonIgnore
    private boolean initialized;

    @Override
    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    public boolean isCreating() {
        return creating;
    }

    public boolean isUpdating() {
        return updating;
    }

    public boolean isInitialized() {
        return initialized;
    }

    protected void markAsCreating(ID id) {
        this.id = id;
        this.creating = true;
        maskAsInitialized();
    }

    protected void markAsUpdating() {
        this.updating = true;
    }

    protected void maskAsInitialized() {
        if (this.initialized) {
            throw new IllegalStateException("You can only once initialized once.");
        }
        this.initialized = true;
    }

    protected void modified() {
        if (isCreating()) return;
        markAsUpdating();
    }

    protected void mustBeInitialized() {
        if (this.initialized) return;
        throw new IllegalStateException("You must be initialize the domain");
    }
}
