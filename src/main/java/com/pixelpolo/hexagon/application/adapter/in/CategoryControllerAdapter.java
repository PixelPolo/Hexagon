package com.pixelpolo.hexagon.application.adapter.in;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.application.exception.CategoryExistException;
import com.pixelpolo.hexagon.application.exception.CategoryNotFoundException;
import com.pixelpolo.hexagon.application.mapper.CategoryDtoMapper;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryServicePort;
import com.pixelpolo.hexagon.infrastructure.utils.LocationUtils;
import com.pixelpolo.hexagon.infrastructure.utils.PaginationUtils;

/**
 * Category REST Controller as an ADAPTER-IN in Hexagonal Architecture.
 * Uses the CategoryServicePort PORT-IN to access the domain.
 * Keeps the domain logic decoupled from external implementations.
 */
@RestController
@RequestMapping("/api/${api.version}/categories")
public class CategoryControllerAdapter {

    private final CategoryServicePort categoryService;
    private final CategoryDtoMapper categoryDtoMapper;
    private final PaginationUtils paginationUtils;
    private final LocationUtils locationUtils;

    @Autowired
    public CategoryControllerAdapter(
            CategoryServicePort categoryService, CategoryDtoMapper categoryDtoMapper,
            PaginationUtils paginationUtils, LocationUtils locationUtils) {
        this.categoryService = categoryService;
        this.categoryDtoMapper = categoryDtoMapper;
        this.paginationUtils = paginationUtils;
        this.locationUtils = locationUtils;
    }

    // GET /api/v_/categories?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // 204 No Content
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        Page<Category> categories = categoryService.getCategories(pageRequest);
        if (categories.isEmpty()) return ResponseEntity.noContent().build();

        // 200 OK
        return ResponseEntity.ok(categoryDtoMapper.toResponseList(categories.getContent()));
    }

    // GET /api/v_/categories/deleted?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping("/deleted")
    public ResponseEntity<List<CategoryResponse>> getAllDeletedCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // 204 No Content
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        Page<Category> categories = categoryService.getDeletedCategories(pageRequest);
        if (categories.isEmpty()) return ResponseEntity.noContent().build();

        // 200 OK
        return ResponseEntity.ok(categoryDtoMapper.toResponseList(categories.getContent()));
    }


    // GET /api/v_/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {

        // 404 Not Found
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException(id);

        // 200 OK
        return ResponseEntity.ok(categoryDtoMapper.toResponse(category.get()));
    }

    // POST /api/v_/categories
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        // 409 Conflict
        Optional<Category> category = categoryService.getCategoryByName(categoryRequest.getName());
        if (category.isPresent()) throw new CategoryExistException(categoryRequest.getName());

        // 201 Created
        Category created = categoryService.createCategory(categoryDtoMapper.toDomain(categoryRequest));
        URI location = locationUtils.getLocation(created.getCategoryId(), "categories");
        return ResponseEntity.created(location).body(categoryDtoMapper.toResponse(created));
    }

    // PUT /api/v_/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest) {

        // 409 Conflict
        Optional<Category> categoryByName = categoryService.getCategoryByName(categoryRequest.getName());
        if (categoryByName.isPresent()) throw new CategoryExistException(categoryRequest.getName());

        // 404 Not Found
        Optional<Category> categoryById = categoryService.getCategoryById(id);
        if (categoryById.isEmpty()) throw new CategoryNotFoundException(id);

        // 200 OK
        Category updated = categoryService.updateCategory(
                categoryById.get(), categoryDtoMapper.toDomain(categoryRequest));
        return ResponseEntity.ok(categoryDtoMapper.toResponse(updated));
    }

    // DELETE /api/v_/categories/{id}?hard=false
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {

        // 404 Not Found
        Optional<Category> categoryById = categoryService.getCategoryById(id);
        if (categoryById.isEmpty()) throw new CategoryNotFoundException(id);

        // 204 No Content
        if (hard) {
            categoryService.hardDeleteCategory(categoryById.get());
            return ResponseEntity.noContent().build();
        }

        // 200 OK
        Category deleted = categoryService.softDeleteCategory(categoryById.get());
        return ResponseEntity.ok(categoryDtoMapper.toResponse(deleted));
    }

}
