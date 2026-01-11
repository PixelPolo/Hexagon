package com.pixelpolo.hexagon.application.exception;

/**
 * Exception thrown when a category is not found.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(long id) {
        super("Category with ID " + id + " not found.");
    }

}
