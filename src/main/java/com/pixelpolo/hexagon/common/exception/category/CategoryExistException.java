package com.pixelpolo.hexagon.common.exception.category;

import com.pixelpolo.hexagon.common.exception.ExistException;

/**
 * Exception thrown when a category with the given name already exists.
 */
public class CategoryExistException extends ExistException {

    public CategoryExistException(String name) {
        super("Category with name '" + name + "' already exists.");
    }

}
