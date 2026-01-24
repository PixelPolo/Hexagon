package com.pixelpolo.hexagon.infrastructure.adapter.in;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for CategoryControllerAdapter.
 * It uses Testcontainers to spin up a PostgreSQL database for testing.<br>
 * <a href="https://java.testcontainers.org/test_framework_integration/junit_5/#extension">Testcontainers documentation</a><br>
 * <a href="https://medium.com/@turanulus/how-to-write-an-integration-test-with-testcontainers-and-postgresql-67425e124753">Interesting article</a><br>
 * Spring Boot Test framework is used with MockMvc for HTTP request simulation.
 * No HTTP calls are actually made through the network, the DispatcherServlet handles them in-memory.
 * All beans (services, repositories, JPA) use real implementations without mocks.
 */
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CategoryControllerAdapterIntegrationTest {

    /*
     * --- TEST CONTAINERS SETUP ---
     * Docker must be running.
     * Static container shared across all tests.
     * Non-static would recreate for each test class.
     */

    @Container
    private static final PostgreSQLContainer<?> PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");
    @Autowired
    private MockMvc mockMvc;

    /*
     * --- FLYWAY SETUP ---
     * Ensure Flyway migrations are applied before each test
     * and cleaned up after each test to maintain a consistent state.
     * application-test.properties must have:
     *      spring.flyway.enabled=true
     *      spring.flyway.clean-disabled=false
     */

    @Autowired
    private Flyway flyway;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PSQL_CONTAINER::getPassword);
    }

    // --- TESTS SETUP ---

    @AfterEach
    void cleanUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Value("${api.version}")
    private String apiVersion;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/" + apiVersion + "/categories";
    }

    // --- TESTS ---

    @Test
    @DisplayName("Testcontainers PostgreSQL container is running")
    void shouldHaveRunningPostgreSQLContainer() {
        assertThat(PSQL_CONTAINER.isRunning()).isTrue();
    }

    @Test
    @DisplayName("GET /api/{version}/categories - Should get all categories")
    void shouldGetAllCategories() throws Exception {
        mockMvc.perform(get(baseUrl)
                                .param("page", "0")
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)));
    }

    @Test
    @DisplayName("GET /api/{version}/categories/deleted - Should get all deleted categories")
    void shouldGetAllDeletedCategories() throws Exception {
        mockMvc.perform(get(baseUrl + "/deleted")
                                .param("page", "0")
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/{version}/categories/1 - Should get category by ID")
    void shouldGetCategoryById() throws Exception {
        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Mobilité"));
    }

    @Test
    @DisplayName("GET /api/{version}/categories/999 - Should return 404 for non-existing category")
    void shouldReturn404ForNonExistingCategory() throws Exception {
        mockMvc.perform(get(baseUrl + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/{version}/categories - Should create a new category")
    void shouldCreateNewCategory() throws Exception {
        mockMvc.perform(post(baseUrl)
                                .contentType("application/json")
                                .content("{\"name\":\"New Category\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").isNumber())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    @Test
    @DisplayName("POST /api/{version}/categories - Should return 409 for existing category")
    void shouldReturn409ForExistingCategory() throws Exception {
        mockMvc.perform(post(baseUrl)
                                .contentType("application/json")
                                .content("{\"name\":\"Mobilité\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /api/{version}/categories/1 - Should update category")
    void shouldUpdateCategory() throws Exception {
        mockMvc.perform(put(baseUrl + "/1")
                                .contentType("application/json")
                                .content("{\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    @Test
    @DisplayName("PUT /api/{version}/categories/1 - Should return 409 for updating to existing category name")
    void shouldReturn409ForUpdatingToExistingCategoryName() throws Exception {
        mockMvc.perform(put(baseUrl + "/1")
                                .contentType("application/json")
                                .content("{\"name\":\"Logement\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /api/{version}/categories/999 - Should return 404 for updating non-existing category")
    void shouldReturn404ForUpdatingNonExistingCategory() throws Exception {
        mockMvc.perform(put(baseUrl + "/999")
                                .contentType("application/json")
                                .content("{\"name\":\"Updated Category\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/{version}/categories/1 - Should soft delete category")
    void shouldSoftDeleteCategory() throws Exception {
        // Create a category to delete and get its location
        String location = mockMvc.perform(post(baseUrl)
                                  .contentType("application/json")
                                  .content("{\"name\":\"Category to be deleted\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        // Soft delete the category
        assert location != null;
        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        // Verify the category is soft deleted (should not be found)
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/{version}/categories/1 - Should hard delete category")
    void shouldHardDeleteCategory() throws Exception {
        // Create a category to delete and get its location
        String location = mockMvc.perform(post(baseUrl)
                                    .contentType("application/json")
                                    .content("{\"name\":\"Category to be deleted\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        // Hard delete the category
        assert location != null;
        mockMvc.perform(delete(location + "?hard=true"))
                .andExpect(status().isNoContent());

        // Verify the category is deleted
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/{version}/categories/999 - Should return 404 for deleting non-existing category")
    void shouldReturn404ForDeletingNonExistingCategory() throws Exception {
        mockMvc.perform(delete(baseUrl + "/999"))
                .andExpect(status().isNotFound());
    }

}
