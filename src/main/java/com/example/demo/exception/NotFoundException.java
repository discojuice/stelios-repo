package com.example.demo.exception;

/**
 * Exception thrown when a requested resource is not found.
 * This exception represents HTTP 404 Not Found status.
 */
public class NotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a NotFoundException with a message.
     *
     * @param message the error message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a NotFoundException with a message and cause.
     *
     * @param message the error message
     * @param cause the root cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a NotFoundException for a resource by ID.
     *
     * @param resourceName the name of the resource
     * @param id the ID of the resource
     * @return formatted error message
     */
    public static NotFoundException forId(String resourceName, Long id) {
        return new NotFoundException(String.format("%s with id %d not found", resourceName, id));
    }
}