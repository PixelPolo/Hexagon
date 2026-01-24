package com.pixelpolo.hexagon.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.infrastructure.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

     CategoryEntity toEntity(Category category);

     Category toDomain(CategoryEntity entity);

    default Page<Category> toDomainPage(Page<CategoryEntity> entityPage) {
        return entityPage.map(this::toDomain);
    }

    default Page<CategoryEntity> toEntityPage(Page<Category> categoryPage) {
        return categoryPage.map(this::toEntity);
    }

}
