package com.pixelpolo.hexagon.infrastructure.mongo.adapter;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryPort;
import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;
import com.pixelpolo.hexagon.infrastructure.mongo.mapper.CategoryMapperMongo;
import com.pixelpolo.hexagon.infrastructure.mongo.repository.CategoryRepositoryMongo;

import lombok.RequiredArgsConstructor;

/**
 * CategoryAdapterMongo is an implementation of the CategoryPort interface for MongoDB.
 * It serves as an adapter between the domain layer and the MongoDB database.
 * It uses CategoryRepositoryMongo to perform database operations and
 * CategoryMapperMongo to convert between Category domain models and CategoryEntityMongo database entities.
 */
@Component
@RequiredArgsConstructor
@Profile("mongo")
public class CategoryAdapterMongo implements CategoryPort {

    private final CategoryRepositoryMongo categoryRepositoryMongo;
    private final CategoryMapperMongo categoryMapperMongo;

    @Override
    public Category save(Category category) {
        CategoryEntityMongo entity = categoryMapperMongo.toEntity(category);
        if (entity.getCategoryId() == null) {
            entity.setCategoryId(System.currentTimeMillis()); // NoSql doesn't support auto-increment
        }
        categoryRepositoryMongo.save(entity);
        return categoryMapperMongo.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryEntityMongo> entities = categoryRepositoryMongo.findAllByDeletionDateIsNull(pageable);
        return categoryMapperMongo.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryEntityMongo> entities = categoryRepositoryMongo.findAllByDeletionDateIsNotNull(pageable);
        return categoryMapperMongo.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapperMongo.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryEntityMongo entity = categoryRepositoryMongo.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryMapperMongo.toDomain(entity);
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
