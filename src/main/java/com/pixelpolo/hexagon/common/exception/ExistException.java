package com.pixelpolo.hexagon.common.exception;

/**
 * Exception thrown when an entity already exists.
 */
public class ExistException extends RuntimeException {

    public ExistException(String message) {
        super(message);
    }

}
