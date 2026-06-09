package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

/**
 * Global exception handler for REST API.
 * Handles all exceptions thrown by controllers and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundException - HTTP 404.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Handles BadRequestException - HTTP 400.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Handles invalid method arguments - HTTP 400.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String message = String.format("Invalid argument: %s should be %s",
                ex.getName(), ex.getRequiredType().getSimpleName());
        return buildResponse(HttpStatus.BAD_REQUEST, message, req.getRequestURI());
    }

    /**
     * Handles 404 Not Found - HTTP 404.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest req) {
        String message = String.format("Endpoint not found: %s %s", ex.getHttpMethod(), ex.getRequestURL());
        return buildResponse(HttpStatus.NOT_FOUND, message, req.getRequestURI());
    }

    /**
     * Handles IllegalArgumentException - HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Handles all other exceptions - HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred", req.getRequestURI());
    }

    /**
     * Builds an ApiError response.
     */
    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, String path) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        return ResponseEntity.status(status).body(body);
    }
}