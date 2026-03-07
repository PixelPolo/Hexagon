package com.pixelpolo.hexagon.application.exception;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

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
    public ResponseEntity<String> handleExist(ExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Bad request Exception Handler
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // SQL Exception Handler
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handleSQLException(SQLException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", ex.getMessage()
                ));
    }

}