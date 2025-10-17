package com.example.usermanagement;

import com.example.usermanagement.UsermanagementApp;
import com.example.usermanagement.config.AsyncSyncConfiguration;
import com.example.usermanagement.config.JacksonConfiguration;
import com.example.usermanagement.config.EmbeddedSQL;
import com.example.usermanagement.config.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {UsermanagementApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class})
@EmbeddedSQL
public @interface IntegrationTest {
}
