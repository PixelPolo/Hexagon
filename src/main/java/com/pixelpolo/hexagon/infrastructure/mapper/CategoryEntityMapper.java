package com.pixelpolo.hexagon.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.infrastructure.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

    // -------------------------------------------------
    // --- This doesn't map correctly with MapStruct ---
    // -------------------------------------------------
    // CategoryEntity toEntity(Category category);
    // Category toDomain(CategoryEntity entity);
    // -------------------------------------------------

    default Category toDomain(CategoryEntity categoryRequest) {
        if (categoryRequest == null) return null;
        return Category.builder()
                .categoryId(categoryRequest.getCategoryId())
                .name(categoryRequest.getName())
                .deletionDate(categoryRequest.getDeletionDate())
                .build();
    }

    default CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        return CategoryEntity.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .deletionDate(category.getDeletionDate())
                .build();
    }

    default Page<Category> toDomainPage(Page<CategoryEntity> entityPage) {
        return entityPage.map(this::toDomain);
    }

    default Page<CategoryEntity> toEntityPage(Page<Category> categoryPage) {
        return categoryPage.map(this::toEntity);
    }

}
