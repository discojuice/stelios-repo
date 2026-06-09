package com.example.demo.exception;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void testApiErrorCreation() {
        Instant now = Instant.now();
        ApiError error = new ApiError(now, 404, "Not Found", "Resource not found", "/api/test");

        assertEquals(now, error.timestamp());
        assertEquals(404, error.status());
        assertEquals("Not Found", error.error());
        assertEquals("Resource not found", error.message());
        assertEquals("/api/test", error.path());
    }

    @Test
    void testApiErrorValidation_Valid() {
        ApiError error = new ApiError(
                Instant.now(),
                400,
                "Bad Request",
                "Invalid data",
                "/api/requests"
        );

        assertTrue(error.isValid());
    }

    @Test
    void testApiErrorValidation_NullTimestamp() {
        ApiError error = new ApiError(
                null,
                404,
                "Not Found",
                "Not found",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_ZeroStatus() {
        ApiError error = new ApiError(
                Instant.now(),
                0,
                "Error",
                "Error message",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_NegativeStatus() {
        ApiError error = new ApiError(
                Instant.now(),
                -1,
                "Error",
                "Error message",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_NullError() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                null,
                "Not found",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_BlankError() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "  ",
                "Not found",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_NullMessage() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "Not Found",
                null,
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_BlankMessage() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "Not Found",
                "   ",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_NullPath() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "Not Found",
                "Not found",
                null
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorValidation_BlankPath() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "Not Found",
                "Not found",
                ""
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorWith500Status() {
        ApiError error = new ApiError(
                Instant.now(),
                500,
                "Internal Server Error",
                "Unexpected error",
                "/api/test"
        );

        assertTrue(error.isValid());
        assertEquals(500, error.status());
    }

    @Test
    void testApiErrorWith200Status() {
        ApiError error = new ApiError(
                Instant.now(),
                200,
                "OK",
                "Success",
                "/api/test"
        );

        assertTrue(error.isValid());
        assertEquals(200, error.status());
    }

    @Test
    void testMultipleApiErrors() {
        ApiError error1 = new ApiError(Instant.now(), 404, "Not Found", "User not found", "/api/users/1");
        ApiError error2 = new ApiError(Instant.now(), 400, "Bad Request", "Invalid input", "/api/requests");
        ApiError error3 = new ApiError(Instant.now(), 500, "Internal Server Error", "Database error", "/api/test");

        assertTrue(error1.isValid());
        assertTrue(error2.isValid());
        assertTrue(error3.isValid());
        assertEquals(404, error1.status());
        assertEquals(400, error2.status());
        assertEquals(500, error3.status());
    }

    @Test
    void testApiErrorAllValidStatusCodes() {
        int[] validStatuses = {200, 201, 204, 400, 401, 403, 404, 409, 500, 502, 503};

        for (int status : validStatuses) {
            ApiError error = new ApiError(
                    Instant.now(),
                    status,
                    "Status " + status,
                    "Test message",
                    "/api/test"
            );
            assertTrue(error.isValid());
            assertEquals(status, error.status());
        }
    }

    @Test
    void testApiErrorTimestampNotNull() {
        Instant now = Instant.now();
        ApiError error = new ApiError(now, 200, "OK", "Success", "/api/test");

        assertNotNull(error.timestamp());
        assertEquals(now, error.timestamp());
    }

    @Test
    void testApiErrorRecordImmutability() {
        Instant now = Instant.now();
        ApiError error = new ApiError(now, 404, "Not Found", "Not found", "/api/test");

        // Records are immutable, so we can verify the values don't change
        assertEquals(404, error.status());
        assertEquals(404, error.status()); // Same value on second access
    }

    @Test
    void testApiErrorWithEmptyStringError() {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "",
                "Not found",
                "/api/test"
        );

        assertFalse(error.isValid());
    }

    @Test
    void testApiErrorWithLongMessage() {
        String longMessage = "This is a very long error message that explains in detail what went wrong with the request " +
                "and provides suggestions for how to fix the issue. It can be quite lengthy.";
        
        ApiError error = new ApiError(
                Instant.now(),
                400,
                "Bad Request",
                longMessage,
                "/api/test"
        );

        assertTrue(error.isValid());
        assertEquals(longMessage, error.message());
    }

    @Test
    void testApiErrorWithSpecialCharactersInMessage() {
        String specialMessage = "Error: Invalid & special @characters #in %message $with ^symbols";
        
        ApiError error = new ApiError(
                Instant.now(),
                400,
                "Bad Request",
                specialMessage,
                "/api/test"
        );

        assertTrue(error.isValid());
        assertEquals(specialMessage, error.message());
    }
}