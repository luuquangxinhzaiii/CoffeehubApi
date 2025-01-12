package com.ali.coffeehub.service.business;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * Domain Event
 */
@SuppressWarnings("unused")
public class DomainEvent implements Serializable {

    /**
     *  Event name
     */
    @NotNull
    private String name;

    /**
     *  Event payload, meta data for event
     */
    private Object payload;

    /**
     * The time when the event has been created at
     */
    private Instant createdAt;

    public DomainEvent(String name, Object payload) {
        this.name = name;
        this.payload = payload;
        this.createdAt = Instant.now();
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
