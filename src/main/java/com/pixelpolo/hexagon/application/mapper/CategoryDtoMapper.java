package com.pixelpolo.hexagon.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.domain.model.Category;

/**
 * Mapper interface for converting between Category and DTO.
 */
@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

    CategoryResponse toResponse(Category category);

    Category toDomain(CategoryRequest request);

    List<CategoryResponse> toResponseList(List<Category> categories);

    List<Category> toDomainList(List<CategoryRequest> categories);

}
