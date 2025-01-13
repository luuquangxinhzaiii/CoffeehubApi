package com.ali.coffeehub.brew.service.business;

import com.ali.coffeehub.service.business.AbstractAggregateRoot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Component
@Scope("prototype")
public class BrewAggregateRoot extends AbstractAggregateRoot<Long, Integer> implements Serializable {
    public static final String AGGREGATE_NAME = "Brew";

    private final ApplicationContext applicationContext;
    private final TransactionManager transactionManager;

    public BrewAggregateRoot(ApplicationContext applicationContext, TransactionManager transactionManager) {
        this.applicationContext = applicationContext;
        this.transactionManager = transactionManager;
    }

    @Transactional
    @Component("BrewTransactionManager")
    public static class TransactionManager {
        public static final String AGGREGATE_NAME = "BrewTransactionManager";
    }
}
