package com.pixelpolo.hexagon.application.exception;

/**
 * Exception thrown when a category with the given name already exists.
 */
public class CategoryExistException extends RuntimeException {

    public CategoryExistException(String name) {
        super("Category with name '" + name + "' already exists.");
    }

}
