package com.pixelpolo.hexagon.infrastructure.mongo.adapter.out;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;
import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;
import com.pixelpolo.hexagon.infrastructure.mongo.mapper.CategoryEntityMapperMongo;
import com.pixelpolo.hexagon.infrastructure.mongo.repository.CategoryRepositoryMongo;

import lombok.RequiredArgsConstructor;

/**
 * Category repository as an ADAPTER-OUT in Hexagonal Architecture.
 * Implements the CategoryRepositoryPort PORT-OUT to interact with the persistence layer.
 * Uses CategoryRepositoryMongo for database operations (easy to swap with another implementation).
 * Keeps the domain logic decoupled from external implementations.
 */
@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class CategoryRepositoryAdapterMongo implements CategoryRepositoryPort {

    private final CategoryRepositoryMongo categoryRepositoryMongo;
    private final CategoryEntityMapperMongo categoryEntityMapperMongo;

    @Override
    public Category persist(Category category) {
        CategoryEntityMongo entity = categoryEntityMapperMongo.toEntity(category);
        if (entity.getCategoryId() == null) {
            entity.setCategoryId(System.currentTimeMillis()); // NoSql doesn't support auto-increment
        }
        categoryRepositoryMongo.save(entity);
        return categoryEntityMapperMongo.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryEntityMongo> entities = categoryRepositoryMongo.findAllByDeletionDateIsNull(pageable);
        return categoryEntityMapperMongo.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryEntityMongo> entities = categoryRepositoryMongo.findAllByDeletionDateIsNotNull(pageable);
        return categoryEntityMapperMongo.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryEntityMapperMongo.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryEntityMapperMongo.toDomain(entity);
    }

    @Override
    public void softDelete(long id) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        entity.setDeletionDate(LocalDateTime.now());
        categoryRepositoryMongo.save(entity);
    }

    @Override
    public void hardDelete(long id) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryRepositoryMongo.delete(entity);
    }

}
