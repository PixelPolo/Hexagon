package com.pixelpolo.hexagon.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pixelpolo.hexagon.common.exception.category.CategoryExistException;
import com.pixelpolo.hexagon.common.exception.category.CategoryNotFoundException;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryUseCase;
import com.pixelpolo.hexagon.domain.port.out.CategoryPort;

import lombok.RequiredArgsConstructor;

/**
 * Service class implementing the CategoryUseCase interface.
 * Contains the business logic for managing categories.
 * Interacts with the CategoryPort to perform data persistence and retrieval operations,
 * while ensuring domain rules are respected.
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;

    @Override
    public Category create(Category category) {
        try {
            categoryPort.findByName(category.getName());
            throw new CategoryExistException(category.getName());
        } catch (CategoryNotFoundException e) {
            return categoryPort.save(category);
        }
    }

    @Override
    public Category update(long id, Category request) {
        try {
            categoryPort.findByName(request.getName());
            throw new CategoryExistException(request.getName());
        } catch (CategoryNotFoundException e) {
            Category existing = categoryPort.findById(id);
            existing.setName(request.getName());
            return categoryPort.save(existing);
        }
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryPort.findAll(pageable);
    }

    @Override
    public Page<Category> getAllDeleted(Pageable pageable) {
        return categoryPort.findAllDeleted(pageable);
    }

    @Override
    public Category getById(long id) {
        return categoryPort.findById(id);
    }

    @Override
    public void softDelete(long id) {
        categoryPort.softDelete(id);
    }

    @Override
    public void hardDelete(long id) {
        categoryPort.hardDelete(id);
    }

}
