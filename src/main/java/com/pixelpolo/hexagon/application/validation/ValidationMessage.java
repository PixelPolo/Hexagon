package com.pixelpolo.hexagon.application.validation;

/**
 * Interface containing validation messages for various constraints.
 */
public interface ValidationMessage {

    String NOT_NULL = "The field must not be null.";
    String NOT_BLANK = "The field must not be blank.";
    String MAX_SIZE_128 = "The field must be at most 128 characters long.";

}
