package com.ali.coffeehub;

import com.ali.coffeehub.config.AsyncSyncConfiguration;
import com.ali.coffeehub.config.EmbeddedRedis;
import com.ali.coffeehub.config.EmbeddedSQL;
import com.ali.coffeehub.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { AppCoffeeHubApiApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
