package com.pixelpolo.hexagon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * POJO representing a Category in the system.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    private Long categoryId;
    private String name;
    private LocalDateTime deletionDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }

}
