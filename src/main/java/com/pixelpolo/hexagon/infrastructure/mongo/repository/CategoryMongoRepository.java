package com.pixelpolo.hexagon.infrastructure.mongo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryMongoEntity;

/**
 * MongoDB Repository interface for Category entity.
 * Extends MongoRepository to provide CRUD operations and pagination support.
 */
public interface CategoryMongoRepository extends MongoRepository<CategoryMongoEntity, Long> {

    Page<CategoryMongoEntity> findAllByDeletionDateIsNull(Pageable pageable);

    Page<CategoryMongoEntity> findAllByDeletionDateIsNotNull(Pageable pageable);

    Optional<CategoryMongoEntity> findByCategoryIdAndDeletionDateIsNull(Long id);

    Optional<CategoryMongoEntity> findByNameAndDeletionDateIsNull(String name);

}
