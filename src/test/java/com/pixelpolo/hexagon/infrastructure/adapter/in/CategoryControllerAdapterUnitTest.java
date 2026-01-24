package com.pixelpolo.hexagon.infrastructure.adapter.in;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pixelpolo.hexagon.application.adapter.in.CategoryControllerAdapter;
import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.application.mapper.CategoryDtoMapper;
import com.pixelpolo.hexagon.common.utils.LocationUtils;
import com.pixelpolo.hexagon.common.utils.PaginationUtils;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryServicePort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for CategoryControllerAdapter.
 * Junit is used as the testing framework.
 * Mockito is used for mocking dependencies.
 */
@ExtendWith(MockitoExtension.class)
class CategoryControllerAdapterUnitTest {

    @Mock
    private CategoryServicePort categoryService;

    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @Mock
    private PaginationUtils paginationUtils;

    @Mock
    private LocationUtils locationUtils;

    @InjectMocks
    private CategoryControllerAdapter categoryController;

    private Category category;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        category = Category.builder().categoryId(99L).name("Category").build();
        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryResponse = CategoryResponse.builder().categoryId(99L).name("Category").build();
        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should get all categories")
    void shouldgetAllCategories() {
        // Arrange
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(paginationUtils.buildPageRequest(0, 10, "categoryId", "asc")).thenReturn(pageRequest);
        when(categoryService.getCategories(pageRequest)).thenReturn(categoryPage);
        when(categoryDtoMapper.toResponseList(anyList())).thenReturn(List.of(categoryResponse));

        // Act
        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories(
                0, 10, "categoryId", "asc");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        // Verify
        verify(categoryService).getCategories(pageRequest);
    }

    @Test
    @DisplayName("Should get all deleted categories")
    void shouldGetAllDeletedCategories() {
        // Arrange
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(paginationUtils.buildPageRequest(0, 10, "categoryId", "asc")).thenReturn(pageRequest);
        when(categoryService.getDeletedCategories(pageRequest)).thenReturn(categoryPage);
        when(categoryDtoMapper.toResponseList(anyList())).thenReturn(List.of(categoryResponse));

        // Act
        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllDeletedCategories(
                0, 10, "categoryId", "asc");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        // Verify
        verify(categoryService).getDeletedCategories(pageRequest);
    }

    @Test
    @DisplayName("Should get category by ID")
    void shouldGetCategoryById() {
        // Arrange
        when(categoryService.getCategoryById(99L)).thenReturn(category);
        when(categoryDtoMapper.toResponse(category)).thenReturn(categoryResponse);

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(99L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(99L);
        assertThat(response.getBody().getName()).isEqualTo(category.getName());

        // Verify
        verify(categoryService).getCategoryById(99L);
    }

    @Test
    @DisplayName("Should create a new category")
    void shouldCreateNewCategory() {
        // Arrange
        when(categoryDtoMapper.toDomain(categoryRequest)).thenReturn(category);
        when(categoryService.createCategory(category)).thenReturn(category);
        when(locationUtils.getLocation(99L, "categories")).thenReturn(null); // URI not asserted here
        when(categoryDtoMapper.toResponse(category)).thenReturn(categoryResponse);

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(99L);
        assertThat(response.getBody().getName()).isEqualTo(category.getName());

        // Verify
        verify(categoryService).createCategory(category);
    }

    @Test
    @DisplayName("Should update an existing category")
    void shouldUpdateExistingCategory() {
        // Arrange
        Category updatedCategory = Category.builder().categoryId(99L).name("Category").build();
        CategoryResponse updatedResponse = CategoryResponse.builder().categoryId(99L).name("Updated Category").build();
        when(categoryDtoMapper.toDomain(categoryRequest)).thenReturn(updatedCategory);
        when(categoryService.updateCategory(99L, updatedCategory)).thenReturn(updatedCategory);
        when(categoryDtoMapper.toResponse(updatedCategory)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<CategoryResponse> response = categoryController.updateCategory(99L, categoryRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(99L);
        assertThat(response.getBody().getName()).isEqualTo("Updated Category");

        // Verify
        verify(categoryService).updateCategory(99L, updatedCategory);
    }

    @Test
    @DisplayName("Should soft delete a category")
    void  shouldDeleteCategory() {
        // Act
        ResponseEntity<CategoryResponse> response = categoryController.deleteCategory(99L, false);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        // Verify
        verify(categoryService).softDeleteCategory(99L);
    }

    @Test
    @DisplayName("Should hard delete a category")
    void  shouldHardDeleteCategory() {
        // Act
        ResponseEntity<CategoryResponse> response = categoryController.deleteCategory(99L, true);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        // Verify
        verify(categoryService).hardDeleteCategory(99L);
    }

}
