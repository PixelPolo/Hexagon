package com.pixelpolo.hexagon.infrastructure.adapter.in;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.application.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;
    private final PaginationUtils paginationUtils;
    private final LocationUtils locationUtils;


    @Autowired
    public CategoryControllerAdapter(
            CategoryServicePort categoryService, CategoryMapper categoryMapper,
            PaginationUtils paginationUtils, LocationUtils locationUtils) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.paginationUtils = paginationUtils;
        this.locationUtils = locationUtils;
    }

    // GET /api/v_/categories?page=_&size=_&sortBy=_&sortDir=_
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequest pageRequest = paginationUtils.buildPageRequest(page, size, sortBy, sortDir);
        Page<Category> categories = categoryService.getCategories(pageRequest);

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.ok(categoryMapper.toResponseList(categories.getContent())); // 200 Ok
        }
    }

}
