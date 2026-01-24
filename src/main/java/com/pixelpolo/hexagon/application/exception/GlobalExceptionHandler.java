package com.pixelpolo.hexagon.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pixelpolo.hexagon.common.exception.BadRequestException;
import com.pixelpolo.hexagon.common.exception.ExistException;
import com.pixelpolo.hexagon.common.exception.NotFoundException;

/**
 * Global exception handler for the application layer.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Not Found Exception Handler
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Exist Exception Handler
    @ExceptionHandler(ExistException.class)
    public ResponseEntity<String> handleCategoryExist(ExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Bad request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}