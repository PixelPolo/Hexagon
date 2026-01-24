package com.pixelpolo.hexagon.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pixelpolo.hexagon.infrastructure.entity.CategoryEntity;

/**
 * JPA Repository interface for Category entity.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 */
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    Page<CategoryEntity> findAllByDeletionDateIsNull(Pageable pageable);

    Page<CategoryEntity> findAllByDeletionDateIsNotNull(Pageable pageable);

    Optional<CategoryEntity> findByCategoryIdAndDeletionDateIsNull(Long id);

    Optional<CategoryEntity> findByNameAndDeletionDateIsNull(String name);

}
