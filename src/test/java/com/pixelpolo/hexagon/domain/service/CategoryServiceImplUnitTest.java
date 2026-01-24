package com.pixelpolo.hexagon.domain.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplUnitTest {

    // Only mocking the PORT-OUT dependency
    @Mock
    private CategoryRepositoryPort categoryRepository;

    // Testing the PORT-IN implementation
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        // Note we don't use the framework to inject dependencies
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    @DisplayName("Should find all categories")
    void shouldFindAllCategories() {
        // Arrange
        Category categoryOne = Category.builder().name("Category 1").build();
        Category categoryTwo = Category.builder().name("Category 2").build();
        List<Category> categoryList = List.of(categoryOne, categoryTwo);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> expectedCategories = new PageImpl<>(categoryList, pageable, categoryList.size());
        when(categoryRepository.findAll(pageable)).thenReturn(expectedCategories);

        // Act
        Page<Category> resultCategories = categoryService.getCategories(pageable);

        // Assert
        assertThat(resultCategories)
                .hasSize(2)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Category 1", "Category 2");

        // Verify
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should find all deleted categories")
    void shouldFindAllDeletedCategories() {
        // Arrange
        Category categoryOne = Category.builder().name("Deleted Category 1").build();
        List<Category> categoryList = List.of(categoryOne);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> expectedCategories = new PageImpl<>(categoryList, pageable, categoryList.size());
        when(categoryRepository.findAllDeleted(pageable)).thenReturn(expectedCategories);

        // Act
        Page<Category> resultCategories = categoryService.getDeletedCategories(pageable);

        // Assert
        assertThat(resultCategories)
                .hasSize(1)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Deleted Category 1");

        // Verify
        verify(categoryRepository).findAllDeleted(pageable);
    }

    @Test
    @DisplayName("Should find category by ID")
    void shouldFindCategoryById() {
        // Arrange
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category 1")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(category);

        // Act
        Category resultCategory = categoryService.getCategoryById(1L);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo("Category 1");

        // Verify
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create a new category")
    void shouldCreateNewCategory() {
        // Arrange
        Category category = Category.builder().name("New Category").build();
        when(categoryRepository.findByName("New Category")).thenThrow(CategoryNotFoundException.class);
        when(categoryRepository.persist(category)).thenReturn(category);

        // Act
        Category resultCategory = categoryService.createCategory(category);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo("New Category");

        // Verify
        verify(categoryRepository).persist(category);
    }

    @Test
    @DisplayName("Should update an existing category")
    void shouldUpdateExistingCategory() {
        // Arrange
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category")
                .build();
        Category update = Category.builder()
                .name("Updated Category")
                .build();
        when(categoryRepository.findByName("Updated Category")).thenThrow(CategoryNotFoundException.class);
        when(categoryRepository.findById(1L)).thenReturn(category);
        when(categoryRepository.persist(category)).thenReturn(category);

        // Act
        Category resultCategory = categoryService.updateCategory(1L, update);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo("Updated Category");

        // Verify
        verify(categoryRepository).persist(category);
    }

    @Test
    @DisplayName("Should soft delete an existing category")
    void shouldSoftDeleteExistingCategory() {
        // Arrange
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category to be deleted")
                .build();

        // Act
        categoryService.softDeleteCategory(category.getCategoryId());

        // Verify
        verify(categoryRepository).softDelete(1L);
    }

    @Test
    @DisplayName("Should hard delete an existing category")
    void shouldHardDeleteExistingCategory() {
        // Arrange
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category to be deleted")
                .build();

        // Act
        categoryService.hardDeleteCategory(category.getCategoryId());

        // Verify
        verify(categoryRepository).hardDelete(1L);
    }

}
