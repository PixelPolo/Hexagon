package com.pixelpolo.hexagon.domain.port.in;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category Service Interface as a PORT-IN in Hexagonal Architecture.
 * Defines the operations related to Category management.
 * Keeps the domain logic decoupled from external implementations.
 */
public interface CategoryServicePort {

    Page<Category> getCategories(Pageable pageable);

    Page<Category> getDeletedCategories(Pageable pageable);

    Optional<Category> getCategoryById(long id);

    Optional<Category> getCategoryByName(String name);

    Category createCategory(Category category);

    Category updateCategory(Category existing, Category request);

    Category softDeleteCategory(Category category);

    void hardDeleteCategory(Category category);

}
