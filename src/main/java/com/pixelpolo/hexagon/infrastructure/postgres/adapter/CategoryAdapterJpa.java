package com.pixelpolo.hexagon.infrastructure.postgres.adapter;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryPort;
import com.pixelpolo.hexagon.infrastructure.postgres.entity.CategoryEntityJpa;
import com.pixelpolo.hexagon.infrastructure.postgres.mapper.CategoryMapperJpa;
import com.pixelpolo.hexagon.infrastructure.postgres.repository.CategoryRepositoryJpa;

import lombok.RequiredArgsConstructor;

/**
 * CategoryAdapterJpa is a JPA implementation of the CategoryPort interface.
 * It serves as an adapter between the domain layer and the PostgreSQL database.
 * It uses CategoryRepositoryJpa to perform database operations and
 * CategoryMapperJpa to convert between Category domain models and CategoryEntityJpa database entities.
 */
@Component
@RequiredArgsConstructor
@Profile("postgres")
public class CategoryAdapterJpa implements CategoryPort {

    private final CategoryRepositoryJpa categoryRepositoryJpa;
    private final CategoryMapperJpa categoryMapperJpa;

    @Override
    public Category save(Category category) {
        CategoryEntityJpa entity = categoryMapperJpa.toEntity(category);
        categoryRepositoryJpa.save(entity);
        return categoryMapperJpa.toDomain(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<CategoryEntityJpa> entities = categoryRepositoryJpa.findAllByDeletionDateIsNull(pageable);
        return categoryMapperJpa.toDomainPage(entities);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        Page<CategoryEntityJpa> entities = categoryRepositoryJpa.findAllByDeletionDateIsNotNull(pageable);
        return categoryMapperJpa.toDomainPage(entities);
    }

    @Override
    public Category findById(long id) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findByCategoryIdAndDeletionDateIsNull(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapperJpa.toDomain(entity);
    }

    @Override
    public Category findByName(String name) {
        CategoryEntityJpa entity = categoryRepositoryJpa.findByNameAndDeletionDateIsNull(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryMapperJpa.toDomain(entity);
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
