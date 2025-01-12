package com.ali.coffeehub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    {
        "com.ali.coffeehub.repository",
        "com.ali.coffeehub.brew.repository",
        "com.ali.coffeehub.cafeteria.repository",
        "com.ali.coffeehub.roaster.repository",
        "com.ali.coffeehub.article.repository",
    }
)
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {}
