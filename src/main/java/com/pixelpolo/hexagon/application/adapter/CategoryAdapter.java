package com.pixelpolo.hexagon.application.adapter;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.application.mapper.CategoryMapper;
import com.pixelpolo.hexagon.domain.port.in.CategoryUseCase;

import lombok.RequiredArgsConstructor;

/**
 * Adapter class for Category operations.
 * Acts as a bridge between the application layer and the domain layer.
 * It uses the CategoryUseCase to perform operations and
 * the CategoryMapper to convert between domain models and DTOs.
 */
@Component
@RequiredArgsConstructor
public class CategoryAdapter {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        return categoryMapper.toResponse(categoryUseCase.create(categoryMapper.toDomain(categoryRequest)));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        return categoryMapper.toResponse(categoryUseCase.update(id, categoryMapper.toDomain(categoryRequest)));
    }

    public List<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryMapper.toResponseList(categoryUseCase.getAll(pageable).getContent());
    }

    public List<CategoryResponse> getAllDeletedCategories(Pageable pageable) {
        return categoryMapper.toResponseList(categoryUseCase.getAllDeleted(pageable).getContent());
    }

    public CategoryResponse getCategoryById(long id) {
        return categoryMapper.toResponse(categoryUseCase.getById(id));
    }

    public void softDeleteCategory(long id) {
        categoryUseCase.softDelete(id);
    }

    public void hardDeleteCategory(long id) {
        categoryUseCase.hardDelete(id);
    }

}
