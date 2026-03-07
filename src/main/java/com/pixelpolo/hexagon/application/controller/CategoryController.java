package com.pixelpolo.hexagon.application.controller;

import java.net.URI;
import java.util.List;

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

import com.pixelpolo.hexagon.application.adapter.CategoryAdapter;
import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.common.utils.LocationUtils;
import com.pixelpolo.hexagon.common.utils.PaginationUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Category REST Controller.
 * Handles HTTP requests for Category operations and delegates to the CategoryAdapter.
 */
@RestController
@RequestMapping("/api/${api.version}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryAdapter categoryAdapter;
    private final PaginationUtils paginationUtils;
    private final LocationUtils locationUtils;

    // GET /api/v_/categories?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        return ResponseEntity.ok(categoryAdapter.getAllCategories(pageRequest));
    }

    // GET /api/v_/categories/deleted?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping("/deleted")
    public ResponseEntity<List<CategoryResponse>> getAllDeletedCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        return ResponseEntity.ok(categoryAdapter.getAllDeletedCategories(pageRequest));
    }

    // GET /api/v_/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryAdapter.getCategoryById(id));
    }

    // POST /api/v_/categories
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse created = categoryAdapter.createCategory(categoryRequest);
        URI location = locationUtils.getLocation(created.getCategoryId(), "categories");
        return ResponseEntity.created(location).body(created);
    }

    // PUT /api/v_/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryAdapter.updateCategory(id, categoryRequest));
    }

    // DELETE /api/v_/categories/{id}?hard=false
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            categoryAdapter.hardDeleteCategory(id);
        } else {
            categoryAdapter.softDeleteCategory(id);
        }
        return ResponseEntity.noContent().build();
    }

}
