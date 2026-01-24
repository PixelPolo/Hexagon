package com.pixelpolo.hexagon.application.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

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
import com.pixelpolo.hexagon.application.mapper.CategoryDtoMapper;
import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.domain.port.in.CategoryServicePort;
import com.pixelpolo.hexagon.common.utils.LocationUtils;
import com.pixelpolo.hexagon.common.utils.PaginationUtils;

/**
 * Category REST Controller as an ADAPTER-IN in Hexagonal Architecture.
 * Uses the CategoryServicePort PORT-IN to access the domain.
 * Keeps the domain logic decoupled from external implementations.
 */
@RestController
@RequestMapping("/api/${api.version}/categories")
@RequiredArgsConstructor
public class CategoryControllerAdapter {

    private final CategoryServicePort categoryService;
    private final CategoryDtoMapper categoryDtoMapper;
    private final PaginationUtils paginationUtils;
    private final LocationUtils locationUtils;

    // GET /api/v_/categories?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // 200 OK
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        Page<Category> categories = categoryService.getCategories(pageRequest);
        return ResponseEntity.ok(categoryDtoMapper.toResponseList(categories.getContent()));
    }

    // GET /api/v_/categories/deleted?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping("/deleted")
    public ResponseEntity<List<CategoryResponse>> getAllDeletedCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // 200 OK
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        Page<Category> categories = categoryService.getDeletedCategories(pageRequest);
        return ResponseEntity.ok(categoryDtoMapper.toResponseList(categories.getContent()));
    }

    // GET /api/v_/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {

        // 200 OK
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDtoMapper.toResponse(category));
    }

    // POST /api/v_/categories
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

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

        // 200 OK
        Category updated = categoryService.updateCategory(id, categoryDtoMapper.toDomain(categoryRequest));
        return ResponseEntity.ok(categoryDtoMapper.toResponse(updated));
    }

    // DELETE /api/v_/categories/{id}?hard=false
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {

        // 204 No Content
        if (hard) {
            categoryService.hardDeleteCategory(id);
        } else {
            categoryService.softDeleteCategory(id);
        }
        return ResponseEntity.noContent().build();
    }

}
