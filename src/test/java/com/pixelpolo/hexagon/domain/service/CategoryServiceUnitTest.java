package com.pixelpolo.hexagon.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.pixelpolo.hexagon.domain.port.out.CategoryPort;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUnitTest {

    private static final Long CATEGORY_ID_ONE = 1L;
    private static final Long CATEGORY_ID_TWO = 2L;
    private static final String CATEGORY_NAME_ONE = "Category 1";
    private static final String CATEGORY_NAME_TWO = "Category 2";
    private static final Category CATEGORY_ONE = Category.builder().categoryId(CATEGORY_ID_ONE).name(CATEGORY_NAME_ONE).build();
    private static final Category CATEGORY_TWO = Category.builder().categoryId(CATEGORY_ID_TWO).name(CATEGORY_NAME_TWO).build();
    private static final List<Category> CATEGORIES = List.of(CATEGORY_ONE, CATEGORY_TWO);

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final Page<Category> CATEGORY_PAGE = new PageImpl<>(List.of(CATEGORY_ONE, CATEGORY_TWO), PAGEABLE, CATEGORIES.size());

    // Only mocking the PORT OUT dependency
    @Mock
    private CategoryPort categoryPort;

    // Testing the PORT IN implementation
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        // Note we don't use the framework to inject dependencies
        categoryService = new CategoryService(categoryPort);
    }

    @Test
    @DisplayName("Should find all categories")
    void shouldFindAllCategories() {
        // Arrange
        when(categoryPort.findAll(PAGEABLE)).thenReturn(CATEGORY_PAGE);

        // Act
        Page<Category> resultCategories = categoryService.getAll(PAGEABLE);

        // Assert
        assertThat(resultCategories)
                .hasSize(CATEGORIES.size())
                .extracting(Category::getName)
                .containsExactlyInAnyOrder(CATEGORY_NAME_ONE, CATEGORY_NAME_TWO);

        // Verify
        verify(categoryPort).findAll(PAGEABLE);
    }

    @Test
    @DisplayName("Should find all deleted categories")
    void shouldFindAllDeletedCategories() {
        // Arrange
        when(categoryPort.findAllDeleted(PAGEABLE)).thenReturn(CATEGORY_PAGE);

        // Act
        Page<Category> resultCategories = categoryService.getAllDeleted(PAGEABLE);

        // Assert
        assertThat(resultCategories)
                .hasSize(CATEGORIES.size())
                .extracting(Category::getName)
                .containsExactlyInAnyOrder(CATEGORY_NAME_ONE, CATEGORY_NAME_TWO);

        // Verify
        verify(categoryPort).findAllDeleted(PAGEABLE);
    }

    @Test
    @DisplayName("Should find category by ID")
    void shouldFindCategoryById() {
        // Arrange
        when(categoryPort.findById(CATEGORY_ID_ONE)).thenReturn(CATEGORY_ONE);

        // Act
        Category resultCategory = categoryService.getById(1L);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo(CATEGORY_NAME_ONE);

        // Verify
        verify(categoryPort).findById(1L);
    }

    @Test
    @DisplayName("Should create a new category")
    void shouldCreateNewCategory() {
        // Arrange
        when(categoryPort.findByName(CATEGORY_NAME_ONE)).thenThrow(CategoryNotFoundException.class);
        when(categoryPort.save(CATEGORY_ONE)).thenReturn(CATEGORY_ONE);

        // Act
        Category resultCategory = categoryService.create(CATEGORY_ONE);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo(CATEGORY_NAME_ONE);

        // Verify
        verify(categoryPort).save(CATEGORY_ONE);
    }

    @Test
    @DisplayName("Should update an existing category")
    void shouldUpdateExistingCategory() {
        // Arrange
        when(categoryPort.findByName(CATEGORY_NAME_TWO)).thenThrow(CategoryNotFoundException.class);
        when(categoryPort.findById(CATEGORY_ID_ONE)).thenReturn(CATEGORY_ONE);
        when(categoryPort.save(CATEGORY_ONE)).thenReturn(CATEGORY_ONE);

        // Act
        Category resultCategory = categoryService.update(CATEGORY_ID_ONE, CATEGORY_TWO);

        // Assert
        assertThat(resultCategory)
                .extracting(Category::getName)
                .isEqualTo(CATEGORY_NAME_TWO);

        // Verify
        verify(categoryPort).save(CATEGORY_ONE);
    }

    @Test
    @DisplayName("Should soft delete an existing category")
    void shouldSoftDeleteExistingCategory() {
        // Act
        categoryService.softDelete(CATEGORY_ID_ONE);

        // Verify
        verify(categoryPort).softDelete(CATEGORY_ID_ONE);
    }

    @Test
    @DisplayName("Should hard delete an existing category")
    void shouldHardDeleteExistingCategory() {
        // Act
        categoryService.hardDelete(CATEGORY_ID_ONE);

        // Verify
        verify(categoryPort).hardDelete(CATEGORY_ID_ONE);
    }

}
