package com.pixelpolo.hexagon.infrastructure.mongo.adapter.out;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;
import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryMongoEntity;
import com.pixelpolo.hexagon.infrastructure.mongo.mapper.CategoryEntityMongoMapper;
import com.pixelpolo.hexagon.infrastructure.mongo.repository.CategoryMongoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Category repository as an ADAPTER-OUT in Hexagonal Architecture.
 * Implements the CategoryRepositoryPort PORT-OUT to interact with the persistence layer.
 * Uses CategoryMongoRepository for database operations (easy to swap with another implementation).
 * Keeps the domain logic decoupled from external implementations.
 */
@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class CategoryRepositoryMongoAdapter implements CategoryRepositoryPort {

    private final CategoryMongoRepository categoryMongoRepository;
    private final CategoryEntityMongoMapper categoryMongoEntityMapper;

    @Override
    public Category persist(Category category) {
        CategoryMongoEntity entity = categoryMongoEntityMapper.toEntity(category);
        if (entity.getCategoryId() == null) {
            entity.setCategoryId(System.currentTimeMillis());
        }
        categoryMongoRepository.save(entity);
        return categoryMongoEntityMapper.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryMongoEntity> entities = categoryMongoRepository.findAllByDeletionDateIsNull(pageable);
        return categoryMongoEntityMapper.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryMongoEntity> entities = categoryMongoRepository.findAllByDeletionDateIsNotNull(pageable);
        return categoryMongoEntityMapper.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryMongoEntity entity = categoryMongoRepository.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMongoEntityMapper.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryMongoEntity entity = categoryMongoRepository.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryMongoEntityMapper.toDomain(entity);
    }

    @Override
    public void softDelete(long id) {
        CategoryMongoEntity entity = categoryMongoRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        entity.setDeletionDate(LocalDateTime.now());
        categoryMongoRepository.save(entity);
    }

    @Override
    public void hardDelete(long id) {
        CategoryMongoEntity entity = categoryMongoRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryMongoRepository.delete(entity);
    }
}
