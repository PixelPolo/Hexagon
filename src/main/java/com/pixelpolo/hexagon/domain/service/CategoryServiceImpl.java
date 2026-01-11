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
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.persist(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.persist(category);
    }

    @Override
    public Category softDeleteCategory(long id) {
        return categoryRepository.softDelete(id);
    }

    @Override
    public void hardDeleteCategory(long id) {
        categoryRepository.hardDelete(id);
    }

}
