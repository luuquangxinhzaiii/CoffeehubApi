package com.ali.coffeehub.service.business;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate in DDD
 *
 * @param <ID> Identity Type
 * @param <V>  Version Type
 */
@SuppressWarnings("unused")
public class AbstractAggregateRoot<ID extends Serializable, V extends Serializable> extends AbstractDomain<ID> implements Serializable {

    /**
     * Events history
     */
    private final List<DomainEvent> events = new ArrayList<>();

    /**
     * Version Optimistic lock
     */
    private V version;

    public List<DomainEvent> getEvents() {
        return events;
    }

    public V getVersion() {
        return version;
    }

    protected void setVersion(V version) {
        this.version = version;
    }

    /**
     * Add new event
     *
     * @param eventName    The name of event
     * @param eventPayload The payload of event
     */
    protected void addEvent(String eventName, Object eventPayload) {
        if (StringUtils.isBlank(eventName)) {
            throw new IllegalArgumentException("eventName");
        }
        events.add(new DomainEvent(eventName, eventPayload));
    }

    /**
     * Add event except creating
     *
     * @param eventName    The name of event
     * @param eventPayload The payload of event
     */
    protected void addEventExceptCreating(String eventName, Object eventPayload) {
        if (!isCreating()) return;
        addEvent(eventName, eventPayload);
    }
}
