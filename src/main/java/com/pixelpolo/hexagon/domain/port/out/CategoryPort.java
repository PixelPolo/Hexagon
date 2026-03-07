package com.pixelpolo.hexagon.domain.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Category PORT OUT interface.
 * Defines the operations related to Category persistence and retrieval.
 */
public interface CategoryPort {

    Category save(Category category);

    Page<Category> findAll(Pageable pageable);

    Page<Category> findAllDeleted(Pageable pageable);

    Category findById(long id);

    Category findByName(String name);

    void softDelete(long id);

    void hardDelete(long id);

}
