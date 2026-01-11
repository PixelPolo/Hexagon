package com.pixelpolo.hexagon.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pixelpolo.hexagon.domain.model.Category;

/**
 * JPA Repository interface for Category entity.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 */
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByDeletionDateIsNull(Pageable pageable);

    Page<Category> findAllByDeletionDateIsNotNull(Pageable pageable);

}
