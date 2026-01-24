package com.pixelpolo.hexagon.infrastructure.adapter.out;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;
import com.pixelpolo.hexagon.infrastructure.entity.CategoryEntity;
import com.pixelpolo.hexagon.infrastructure.mapper.CategoryEntityMapper;
import com.pixelpolo.hexagon.infrastructure.repository.CategoryJpaRepository;

/**
 * Category repository as an ADAPTER-OUT in Hexagonal Architecture.
 * Implements the CategoryRepositoryPort PORT-OUT to interact with the persistence layer.
 * Uses CategoryJpaRepository for database operations (easy to swap with another implementation).
 * Keeps the domain logic decoupled from external implementations.
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Category persist(Category category) {
        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        categoryJpaRepository.save(entity);
        return categoryEntityMapper.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryEntity> entities = categoryJpaRepository.findAllByDeletionDateIsNull(pageable);
        return categoryEntityMapper.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryEntity> entities = categoryJpaRepository.findAllByDeletionDateIsNotNull(pageable);
        return categoryEntityMapper.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryEntity entity = categoryJpaRepository.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryEntityMapper.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryEntity entity = categoryJpaRepository.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryEntityMapper.toDomain(entity);
    }

    @Override
    public void softDelete(long id) {
        CategoryEntity entity = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        entity.setDeletionDate(LocalDateTime.now());
        categoryJpaRepository.save(entity);
    }

    @Override
    public void hardDelete(long id) {
        CategoryEntity entity = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryJpaRepository.delete(entity);
    }

}
