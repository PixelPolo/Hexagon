package com.pixelpolo.hexagon.infrastructure.mongo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;

/**
 * MongoDB Repository interface for Category entity.
 * Extends MongoRepository to provide CRUD operations and pagination support.
 */
public interface CategoryRepositoryMongo extends MongoRepository<CategoryEntityMongo, Long> {

    Page<CategoryEntityMongo> findAllByDeletionDateIsNull(Pageable pageable);

    Page<CategoryEntityMongo> findAllByDeletionDateIsNotNull(Pageable pageable);

    Optional<CategoryEntityMongo> findByCategoryIdAndDeletionDateIsNull(Long id);

    Optional<CategoryEntityMongo> findByNameAndDeletionDateIsNull(String name);

}
