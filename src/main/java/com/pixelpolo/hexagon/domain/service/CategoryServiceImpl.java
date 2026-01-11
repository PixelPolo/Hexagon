package com.pixelpolo.hexagon.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryServicePort;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;

/**
 * Domain service implementation for Category management.
 * Implements the CategoryServicePort (port-in) to provide business operations.
 * Uses CategoryRepositoryPort (port-out) to interact with persistence layer.
 */
@Service
public class CategoryServiceImpl implements CategoryServicePort {

    private final CategoryRepositoryPort categoryRepository;

    public CategoryServiceImpl(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> getDeletedCategories(Pageable pageable) {
        return categoryRepository.findAllDeleted(pageable);
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.persist(category);
    }

    @Override
    public Category updateCategory(Category existing, Category request) {
        existing.setName(request.getName());
        existing.setDeletionDate(request.getDeletionDate());
        return categoryRepository.persist(existing);
    }

    @Override
    public Category softDeleteCategory(Category category) {
        return categoryRepository.softDelete(category);
    }

    @Override
    public void hardDeleteCategory(Category category) {
        categoryRepository.hardDelete(category);
    }

}
