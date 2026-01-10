package com.pixelpolo.hexagon.domain.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Category categoryOne = new Category();
        categoryOne.setName("Category 1");
        Category categoryTwo = new Category();
        categoryTwo.setName("Category 2");
        List<Category> expectedCategories = List.of(categoryOne, categoryTwo);
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // Act
        List<Category> resultCategories = categoryService.getCategories();

        // Assert
        assertThat(resultCategories)
                .hasSize(2)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Category 1", "Category 2");

        // Verify
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Should find category by ID")
    void shouldFindCategoryById() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Category 1");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> resultCategory = categoryService.getCategoryById(1L);

        // Assert
        assertThat(resultCategory)
                .isPresent()
                .get()
                .extracting(Category::getName)
                .isEqualTo("Category 1");

        // Verify
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create a new category")
    void shouldCreateNewCategory() {
        // Arrange
        Category category = new Category();
        category.setName("New Category");
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
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Updated Category");
        when(categoryRepository.persist(category)).thenReturn(category);

        // Act
        Category resultCategory = categoryService.updateCategory(category);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo("Updated Category");

        // Verify
        verify(categoryRepository).persist(category);
    }

    @Test
    @DisplayName("Should soft delete an existing category")
    void shouldDeleteExistingCategory() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Category to be deleted");
        when(categoryRepository.softDelete(1L)).thenReturn(category);

        // Act
        Category resultCategory = categoryService.softDeleteCategory(1L);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo("Category to be deleted");

        // Verify
        verify(categoryRepository).softDelete(1L);
    }

    @Test
    @DisplayName("Should hard delete an existing category")
    void shouldHardDeleteExistingCategory() {
        // Arrange
        Category expected = new Category();
        expected.setCategoryId(1L);
        expected.setName("Category to be hard deleted");

        // Act
        categoryService.hardDeleteCategory(1L);

        // Verify
        verify(categoryRepository).hardDelete(1L);
    }

}
