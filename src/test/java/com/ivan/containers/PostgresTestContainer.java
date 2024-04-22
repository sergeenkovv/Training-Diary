package com.ivan.containers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgresTestContainer {

    public static final String IMAGE_VERSION = "postgres:16.1";

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(IMAGE_VERSION);

    @BeforeAll
    static void start() {
        container.start();
    }

    @AfterAll
    static void stop() {
        container.stop();
    }
}