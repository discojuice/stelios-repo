package com.example.demo.exception;

import java.time.Instant;

/**
 * API error response structure.
 *
 * @param timestamp the time when the error occurred
 * @param status the HTTP status code
 * @param error the HTTP status reason phrase
 * @param message the error message
 * @param path the request path
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
    /**
     * Validates that all required fields are present.
     *
     * @return true if all fields are valid
     */
    public boolean isValid() {
        return timestamp != null && status > 0 && error != null && !error.isBlank() &&
                message != null && !message.isBlank() && path != null && !path.isBlank();
    }
}