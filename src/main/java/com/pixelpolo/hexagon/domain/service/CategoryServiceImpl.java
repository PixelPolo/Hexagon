package com.pixelpolo.hexagon.domain.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pixelpolo.hexagon.common.exception.category.CategoryExistException;
import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryServicePort;
import com.pixelpolo.hexagon.domain.port.out.CategoryRepositoryPort;

/**
 * Domain service implementation for Category management.
 * Implements the CategoryServicePort (port-in) to provide business operations.
 * Uses CategoryRepositoryPort (port-out) to interact with persistence layer.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryServicePort {

    private final CategoryRepositoryPort categoryRepository;

    @Override
    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> getDeletedCategories(Pageable pageable) {
        return categoryRepository.findAllDeleted(pageable);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        try {
            categoryRepository.findByName(category.getName());
            throw new CategoryExistException(category.getName());
        } catch (CategoryNotFoundException e) {
            return categoryRepository.persist(category);
        }
    }

    @Override
    public Category updateCategory(long id, Category request) {
        try {
            categoryRepository.findByName(request.getName());
            throw new CategoryExistException(request.getName());
        } catch (CategoryNotFoundException e) {
            Category existing = categoryRepository.findById(id);
            existing.setName(request.getName());
            return categoryRepository.persist(existing);
        }
    }

    @Override
    public void softDeleteCategory(long id) {
        categoryRepository.softDelete(id);
    }

    @Override
    public void hardDeleteCategory(long id) {
        categoryRepository.hardDelete(id);
    }

}
