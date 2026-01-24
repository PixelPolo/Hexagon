package com.pixelpolo.hexagon.common.exception.category;

import com.pixelpolo.hexagon.common.exception.NotFoundException;

/**
 * Exception thrown when a category is not found.
 */
public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(long id) {
        super("Category with ID " + id + " not found.");
    }

    public CategoryNotFoundException(String name) {
        super("Category with name '" + name + "' not found.");
    }

}
