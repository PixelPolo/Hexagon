package com.pixelpolo.hexagon.application.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(long id) {
        super("Category with ID " + id + " not found.");
    }

}
