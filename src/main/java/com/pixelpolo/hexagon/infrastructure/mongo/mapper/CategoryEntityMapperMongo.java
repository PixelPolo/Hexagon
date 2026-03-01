package com.pixelpolo.hexagon.infrastructure.mongo.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapperMongo {

    CategoryEntityMongo toEntity(Category category);

    Category toDomain(CategoryEntityMongo entity);

    default Page<Category> toDomainPage(Page<CategoryEntityMongo> entityPage) {
        return entityPage.map(this::toDomain);
    }

    default Page<CategoryEntityMongo> toEntityPage(Page<Category> categoryPage) {
        return categoryPage.map(this::toEntity);
    }

}
