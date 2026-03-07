package com.pixelpolo.hexagon.application.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pixelpolo.hexagon.application.adapter.CategoryAdapter;
import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.common.utils.LocationUtils;
import com.pixelpolo.hexagon.common.utils.PaginationUtils;

/**
 * Unit tests for CategoryController.
 * Junit is used as the testing framework.
 * Mockito is used for mocking dependencies.
 */
@ExtendWith(MockitoExtension.class)
class CategoryControllerUnitTest {

    public static final String SORT_BY = "categoryId";
    public static final String SORT_DIR = "asc";
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final PageRequest PAGE_REQUEST = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

    public static final Long CATEGORY_ID = 99L;
    public static final String CATEGORY_NAME = "Category";
    public static final String PATH_VARIABLE = "categories";
    public static final String LOCATION_URL = "/api/v1/categories/" + CATEGORY_ID;

    public static final CategoryRequest CATEGORY_REQUEST = CategoryRequest.builder().name(CATEGORY_NAME).build();
    public static final CategoryResponse CATEGORY_RESPONSE = CategoryResponse.builder().categoryId(CATEGORY_ID).name(CATEGORY_NAME).build();

    @Mock
    private PaginationUtils paginationUtils;

    @Mock
    private LocationUtils locationUtils;

    @Mock
    private CategoryAdapter categoryAdapter;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    @DisplayName("Should get all categories")
    void shouldgetAllCategories() {
        // Arrange
        when(paginationUtils.buildPageRequest(PAGE_NUMBER, PAGE_SIZE, SORT_BY, SORT_DIR)).thenReturn(PAGE_REQUEST);
        when(categoryAdapter.getAllCategories(PAGE_REQUEST)).thenReturn(List.of(CATEGORY_RESPONSE));

        // Act
        ResponseEntity<List<CategoryResponse>> response =
                categoryController.getAllCategories(PAGE_NUMBER, PAGE_SIZE, SORT_BY, SORT_DIR);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        // Verify
        verify(categoryAdapter).getAllCategories(PAGE_REQUEST);
    }

    @Test
    @DisplayName("Should get all deleted categories")
    void shouldGetAllDeletedCategories() {
        // Arrange
        when(paginationUtils.buildPageRequest(PAGE_NUMBER, PAGE_SIZE, SORT_BY, SORT_DIR)).thenReturn(PAGE_REQUEST);
        when(categoryAdapter.getAllDeletedCategories(PAGE_REQUEST)).thenReturn(List.of(CATEGORY_RESPONSE));

        // Act
        ResponseEntity<List<CategoryResponse>> response =
                categoryController.getAllDeletedCategories(PAGE_NUMBER, PAGE_SIZE, SORT_BY, SORT_DIR);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        // Verify
        verify(categoryAdapter).getAllDeletedCategories(PAGE_REQUEST);
    }

    @Test
    @DisplayName("Should get category by ID")
    void shouldGetCategoryById() {
        // Arrange
        when(categoryAdapter.getCategoryById(99L)).thenReturn(CATEGORY_RESPONSE);

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(CATEGORY_ID);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(response.getBody().getName()).isEqualTo(CATEGORY_NAME);

        // Verify
        verify(categoryAdapter).getCategoryById(99L);
    }

    @Test
    @DisplayName("Should create a new category")
    void shouldCreateNewCategory() {
        // Arrange
        when(categoryAdapter.createCategory(CATEGORY_REQUEST)).thenReturn(CATEGORY_RESPONSE);
        when(locationUtils.getLocation(CATEGORY_ID, PATH_VARIABLE)).thenReturn(URI.create(LOCATION_URL));

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.createCategory(CATEGORY_REQUEST);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(response.getBody().getName()).isEqualTo(CATEGORY_NAME);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo(LOCATION_URL);

        // Verify
        verify(categoryAdapter).createCategory(CATEGORY_REQUEST);
    }

    @Test
    @DisplayName("Should update an existing category")
    void shouldUpdateExistingCategory() {
        // Arrange
        String updatedName = "Updated Category";
        CategoryRequest updateRequest = CategoryRequest.builder().name(updatedName).build();
        CategoryResponse updateResponse = CategoryResponse.builder().categoryId(CATEGORY_ID).name(updatedName).build();
        when(categoryAdapter.updateCategory(CATEGORY_ID, updateRequest)).thenReturn(updateResponse);

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.updateCategory(CATEGORY_ID, updateRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(CATEGORY_ID);
        assertThat(response.getBody().getName()).isEqualTo(updatedName);

        // Verify
        verify(categoryAdapter).updateCategory(CATEGORY_ID, updateRequest);
    }

    @Test
    @DisplayName("Should soft delete a category")
    void  shouldDeleteCategory() {
        // Act
        ResponseEntity<CategoryResponse> response = categoryController.deleteCategory(CATEGORY_ID, false);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        // Verify
        verify(categoryAdapter).softDeleteCategory(CATEGORY_ID);
    }

    @Test
    @DisplayName("Should hard delete a category")
    void  shouldHardDeleteCategory() {
        // Act
        ResponseEntity<CategoryResponse> response = categoryController.deleteCategory(CATEGORY_ID, true);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        // Verify
        verify(categoryAdapter).hardDeleteCategory(CATEGORY_ID);
    }

}
