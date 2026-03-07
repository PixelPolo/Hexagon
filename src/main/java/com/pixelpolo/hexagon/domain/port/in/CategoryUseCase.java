package com.pixelpolo.hexagon.domain.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category PORT IN interface.
 * Defines operations (use cases) related to Category management.
 */
public interface CategoryUseCase {

    Category create(Category category);

    Category update(long id, Category category);

    Page<Category> getAll(Pageable pageable);

    Page<Category> getAllDeleted(Pageable pageable);

    Category getById(long id);

    void softDelete(long id);

    void hardDelete(long id);

}
