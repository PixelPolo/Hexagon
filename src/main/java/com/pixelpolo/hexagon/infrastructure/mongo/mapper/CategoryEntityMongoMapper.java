package com.pixelpolo.hexagon.infrastructure.mongo.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryMongoEntity;

@Mapper(componentModel = "spring")
public interface CategoryEntityMongoMapper {

    CategoryMongoEntity toEntity(Category category);

    Category toDomain(CategoryMongoEntity entity);

    default Page<Category> toDomainPage(Page<CategoryMongoEntity> entityPage) {
        return entityPage.map(this::toDomain);
    }

    default Page<CategoryMongoEntity> toEntityPage(Page<Category> categoryPage) {
        return categoryPage.map(this::toEntity);
    }

}
