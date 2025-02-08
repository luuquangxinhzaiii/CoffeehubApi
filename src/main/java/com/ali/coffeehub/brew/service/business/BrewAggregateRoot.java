package com.ali.coffeehub.brew.service.business;

import com.ali.coffeehub.service.business.AbstractAggregateRoot;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class BrewAggregateRoot extends AbstractAggregateRoot<Long, Integer> implements Serializable {
    public static final String AGGREGATE_NAME = "Brew";

    public static final String CREATE_EVENT = "create";
    public static final String UPDATE_EVENT = "update";
    public static final String DELETE_EVENT = "delete";

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int BUSINESS_VERSION = 1;

    private final ApplicationContext applicationContext;
    private final TransactionManager transactionManager;

    public BrewAggregateRoot(ApplicationContext applicationContext, TransactionManager transactionManager) {
        this.applicationContext = applicationContext;
        this.transactionManager = transactionManager;
    }

    @NotNull
    private Long categoryId;

    @NotNull
    private String name;

    private String description;

    private Integer level;

    private String serving;

    private String iconUri;

    private String imageUri;

    private Boolean deleted;

    private Boolean isPinned;

    @NotNull
    private Instant createdAt;

    private String createdBy;

    private Instant updatedAt;

    private String updatedBy;

    private Set<@NotNull RecipeDomain> recipes = new LinkedHashSet<>();

    private Set<@NotNull StepDomain> steps = new LinkedHashSet<>();

    private Set<@NotNull ToolDomain> tool = new LinkedHashSet<>();

    @Transactional
    @Component("BrewTransactionManager")
    public static class TransactionManager {
        public static final String AGGREGATE_NAME = "BrewTransactionManager";
    }
}
