package com.pixelpolo.hexagon.infrastructure.postgres.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.pixelpolo.hexagon.domain.model.Category;
import com.pixelpolo.hexagon.infrastructure.postgres.entity.CategoryEntityJpa;

/**
 * Mapper interface for converting between Category domain model and CategoryEntityJpa.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapperJpa {

     CategoryEntityJpa toEntity(Category category);

     Category toDomain(CategoryEntityJpa entity);

    default Page<Category> toDomainPage(Page<CategoryEntityJpa> entityPage) {
        return entityPage.map(this::toDomain);
    }

    default Page<CategoryEntityJpa> toEntityPage(Page<Category> categoryPage) {
        return categoryPage.map(this::toEntity);
    }

}
