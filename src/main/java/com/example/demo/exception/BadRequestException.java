package com.example.demo.exception;

/**
 * Exception thrown when a request contains invalid data or parameters.
 * This exception represents HTTP 400 Bad Request status.
 */
public class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a BadRequestException with a message.
     *
     * @param message the error message
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a BadRequestException with a message and cause.
     *
     * @param message the error message
     * @param cause the root cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}