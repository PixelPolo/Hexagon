package com.pixelpolo.hexagon.domain.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category Repository Interface as a PORT-OUT in Hexagonal Architecture.
 * Defines the operations for data persistence related to Category management.
 * Keeps the domain logic decoupled from external implementations.
 */
public interface CategoryRepositoryPort {

    Category persist(Category category);

    Page<Category> findAll(Pageable pageable);

    Page<Category> findAllDeleted(Pageable pageable);

    Category findById(long id);

    Category findByName(String name);

    void softDelete(long id);

    void hardDelete(long id);

}
