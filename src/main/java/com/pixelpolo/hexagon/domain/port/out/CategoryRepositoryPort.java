package com.pixelpolo.hexagon.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category Repository Interface as a PORT-OUT in Hexagonal Architecture.
 * Defines the operations for data persistence related to Category management.
 * Keeps the domain logic decoupled from external implementations.
 */
public interface CategoryRepositoryPort {

    Category persist(Category category);

    List<Category> findAll();

    Optional<Category> findById(long id);

    Category softDelete(Long id);

    void hardDelete(Long id);

}
