package com.ali.coffeehub.service.business;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Sub entity in DDD
 */
@SuppressWarnings("unused")
public abstract class AbstractEntity<TAggregateRoot extends AbstractAggregateRoot<?, ?>, ID extends Serializable>
    extends AbstractDomain<ID>
    implements Serializable {

    /**
     * Reference to aggregate root
     */
    @JsonIgnore
    private TAggregateRoot aggregateRoot;

    public TAggregateRoot getAggregateRoot() {
        return aggregateRoot;
    }

    public void setAggregateRoot(TAggregateRoot aggregateRoot) {
        this.aggregateRoot = aggregateRoot;
    }
}
