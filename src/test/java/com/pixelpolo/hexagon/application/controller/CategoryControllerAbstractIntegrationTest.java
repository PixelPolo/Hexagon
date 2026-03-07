package com.pixelpolo.hexagon.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Abstract class of Integration tests for CategoryController.
 * Spring Boot Test framework is used with MockMvc for HTTP request simulation.
 * No HTTP calls are actually made through the network, the DispatcherServlet handles them in-memory.
 * All beans (services, repositories, JPA) use real implementations without mocks.
 */
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class CategoryControllerAbstractIntegrationTest {

    protected String baseUrl;

    @Value("${api.version}")
    private String apiVersion;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/" + apiVersion + "/categories";
        resetDatabase();
    }

    @Autowired
    protected MockMvc mockMvc;

    protected abstract void resetDatabase();

    // --- TESTS ---

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
        assertThat(location).isNotNull();
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
        assertThat(location).isNotNull();
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
