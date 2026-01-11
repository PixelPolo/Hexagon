package com.pixelpolo.hexagon.infrastructure.adapter.out;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;
import com.pixelpolo.hexagon.infrastructure.repository.CategoryJpaRepository;

/**
 * Category repository as an ADAPTER-OUT in Hexagonal Architecture.
 * Implements the CategoryRepositoryPort PORT-OUT to interact with the persistence layer.
 * Uses CategoryJpaRepository for database operations (easy to swap with another implementation).
 * Keeps the domain logic decoupled from external implementations.
 */
@Repository
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    public CategoryRepositoryAdapter(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Category persist(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryJpaRepository.findAllByDeletionDateIsNull(pageable);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        return categoryJpaRepository.findAllByDeletionDateIsNotNull(pageable);
    }

    @Override
    public Optional<Category> findById(long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }

    @Override
    public Category softDelete(Category category) {
        category.setDeletionDate(LocalDateTime.now());
        return categoryJpaRepository.save(category);
    }

    @Override
    public void hardDelete(Category category) {
        categoryJpaRepository.delete(category);
    }

}
