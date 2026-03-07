package com.pixelpolo.hexagon.application.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * Integration tests for CategoryController.<br>
 * It uses Testcontainers to spin up a PostgreSQL database for testing.<br>
 * <a href="https://java.testcontainers.org/test_framework_integration/junit_5/#extension">Testcontainers documentation</a><br>
 * <a href="https://medium.com/@turanulus/how-to-write-an-integration-test-with-testcontainers-and-postgresql-67425e124753">Interesting article</a><br>
 */
@ActiveProfiles({"test", "postgres"})
class CategoryControllerPostgresIntegrationTest extends CategoryControllerAbstractIntegrationTest {

    // --- TEST CONTAINERS SETUP ---
    // Docker must be running.
    // Static container shared across all tests.
    // Non-static would recreate for each test class.

    @Container
    private static final PostgreSQLContainer<?> PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PSQL_CONTAINER::getPassword);
    }

    // --- TESTS SETUP ---
    // Ensure Flyway migrations are applied before each test
    // and cleaned up after each test to maintain a consistent state.
    // application-test.properties must have:
    //      spring.flyway.enabled=true
    //      spring.flyway.clean-disabled=false

    @Autowired
    private Flyway flyway;

    @Override
    protected void resetDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    // --- TESTS ---

    @Test
    @DisplayName("Testcontainers PostgreSQL container is running")
    void shouldHaveRunningPostgreSQLContainer() {
        assertThat(PSQL_CONTAINER.isRunning()).isTrue();
    }

}
