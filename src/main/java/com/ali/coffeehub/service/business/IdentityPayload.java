package com.ali.coffeehub.service.business;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class IdentityPayload<T extends IdentityGetter<ID>, ID extends Serializable> implements IdentityGetter<ID>, Serializable {

    @JsonIgnore
    private final T source;

    public IdentityPayload(T source) {
        this.source = source;
    }

    @Override
    public ID getId() {
        return source.getId();
    }
}
