package com.pixelpolo.hexagon.infrastructure.postgres.adapter.out;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;
import com.pixelpolo.hexagon.infrastructure.postgres.entity.CategoryEntityJpa;
import com.pixelpolo.hexagon.infrastructure.postgres.mapper.CategoryEntityMapperJpa;
import com.pixelpolo.hexagon.infrastructure.postgres.repository.CategoryRepositoryJpa;

import lombok.RequiredArgsConstructor;

/**
 * Category repository as an ADAPTER-OUT in Hexagonal Architecture.
 * Implements the CategoryRepositoryPort PORT-OUT to interact with the persistence layer.
 * Uses CategoryRepositoryJpa for database operations (easy to swap with another implementation).
 * Keeps the domain logic decoupled from external implementations.
 */
@Repository
@RequiredArgsConstructor
@Profile("postgres")
public class CategoryRepositoryAdapterJpa implements CategoryRepositoryPort {

    private final CategoryRepositoryJpa categoryRepositoryJpa;
    private final CategoryEntityMapperJpa categoryEntityMapperJpa;

    @Override
    public Category persist(Category category) {
        CategoryEntityJpa entity = categoryEntityMapperJpa.toEntity(category);
        categoryRepositoryJpa.save(entity);
        return categoryEntityMapperJpa.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryEntityJpa> entities = categoryRepositoryJpa.findAllByDeletionDateIsNull(pageable);
        return categoryEntityMapperJpa.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryEntityJpa> entities = categoryRepositoryJpa.findAllByDeletionDateIsNotNull(pageable);
        return categoryEntityMapperJpa.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryEntityMapperJpa.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryEntityMapperJpa.toDomain(entity);
    }

    @Override
    public void softDelete(long id) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        entity.setDeletionDate(LocalDateTime.now());
        categoryRepositoryJpa.save(entity);
    }

    @Override
    public void hardDelete(long id) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryRepositoryJpa.delete(entity);
    }

}
