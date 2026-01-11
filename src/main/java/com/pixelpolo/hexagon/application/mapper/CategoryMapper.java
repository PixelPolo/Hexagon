package com.pixelpolo.hexagon.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.pixelpolo.hexagon.application.dto.CategoryRequest;
import com.pixelpolo.hexagon.application.dto.CategoryResponse;
import com.pixelpolo.hexagon.domain.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    List<CategoryResponse> toResponseList(List<Category> categories);

    List<Category> toEntityList(List<CategoryRequest> categories);

}
