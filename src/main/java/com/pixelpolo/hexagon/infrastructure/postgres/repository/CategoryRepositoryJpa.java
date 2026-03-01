package com.pixelpolo.hexagon.infrastructure.postgres.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pixelpolo.hexagon.infrastructure.postgres.entity.CategoryEntityJpa;

/**
 * JPA Repository interface for Category entity.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 */
public interface CategoryRepositoryJpa extends JpaRepository<CategoryEntityJpa, Long> {

    Page<CategoryEntityJpa> findAllByDeletionDateIsNull(Pageable pageable);

    Page<CategoryEntityJpa> findAllByDeletionDateIsNotNull(Pageable pageable);

    Optional<CategoryEntityJpa> findByCategoryIdAndDeletionDateIsNull(Long id);

    Optional<CategoryEntityJpa> findByNameAndDeletionDateIsNull(String name);

}
