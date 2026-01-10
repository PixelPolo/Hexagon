package com.pixelpolo.hexagon.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category Service Interface as a PORT-IN in Hexagonal Architecture.
 * Defines the operations related to Category management.
 * Keeps the domain logic decoupled from external implementations.
 */
public interface CategoryServicePort {

    List<Category> getCategories();

    Optional<Category> getCategoryById(long id);

    Category createCategory(Category category);

    Category updateCategory(Category category);

    Category softDeleteCategory(long id);

    void hardDeleteCategory(long id);

}
