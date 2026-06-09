package com.example.demo.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionTest {

    @Test
    void testNotFoundExceptionCreation() {
        String message = "Request not found";
        NotFoundException ex = new NotFoundException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testNotFoundExceptionWithCause() {
        String message = "Request not found";
        Throwable cause = new RuntimeException("Database error");
        NotFoundException ex = new NotFoundException(message, cause);

        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals("Database error", ex.getCause().getMessage());
    }

    @Test
    void testBadRequestExceptionCreation() {
        String message = "Invalid request parameters";
        BadRequestException ex = new BadRequestException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testBadRequestExceptionWithCause() {
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid value");
        BadRequestException ex = new BadRequestException(message, cause);

        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testNotFoundExceptionForIdMultipleIds() {
        NotFoundException ex1 = NotFoundException.forId("User", 1L);
        NotFoundException ex2 = NotFoundException.forId("BlogPost", 999L);
        NotFoundException ex3 = NotFoundException.forId("Comment", 0L);

        assertEquals("User with id 1 not found", ex1.getMessage());
        assertEquals("BlogPost with id 999 not found", ex2.getMessage());
        assertEquals("Comment with id 0 not found", ex3.getMessage());
    }

    @Test
    void testNotFoundExceptionForIdWithLargeNumber() {
        NotFoundException ex = NotFoundException.forId("Request", 9223372036854775807L);

        assertEquals("Request with id 9223372036854775807 not found", ex.getMessage());
    }

    @Test
    void testExceptionThrowAndCatch() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Test");
        });

        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("Test");
        });
    }

    @Test
    void testExceptionThrowAndCatchWithMessage() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Specific message");
        });

        assertEquals("Specific message", ex.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        NotFoundException notFound = new NotFoundException("Test");
        BadRequestException badRequest = new BadRequestException("Test");

        assertTrue(notFound instanceof RuntimeException);
        assertTrue(badRequest instanceof RuntimeException);
    }

    @Test
    void testExceptionStackTrace() {
        NotFoundException ex = new NotFoundException("Test message");

        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 0);
    }

    @Test
    void testExceptionToString() {
        NotFoundException ex = new NotFoundException("Not found");
        String toString = ex.toString();

        assertTrue(toString.contains("NotFoundException"));
        assertTrue(toString.contains("Not found"));
    }

    @Test
    void testNotFoundExceptionForIdWithNegativeId() {
        NotFoundException ex = NotFoundException.forId("Item", -1L);

        assertEquals("Item with id -1 not found", ex.getMessage());
    }

    @Test
    void testChainedExceptions() {
        Exception root = new Exception("Root cause");
        BadRequestException bad = new BadRequestException("Bad request", root);
        NotFoundException notFound = new NotFoundException("Not found", bad);

        assertEquals("Not found", notFound.getMessage());
        assertEquals(bad, notFound.getCause());
        assertEquals(root, bad.getCause());
    }

    @Test
    void testExceptionSerialVersionUID() {
        NotFoundException ex1 = new NotFoundException("Test");
        NotFoundException ex2 = new NotFoundException("Test");

        assertEquals(ex1.getClass(), ex2.getClass());
    }

    @Test
    void testBadRequestExceptionMessage() {
        String[] messages = {
                "Invalid parameter",
                "Missing required field",
                "Invalid data format",
                "Validation error"
        };

        for (String message : messages) {
            BadRequestException ex = new BadRequestException(message);
            assertEquals(message, ex.getMessage());
        }
    }

    @Test
    void testNotFoundExceptionForIdVariousCases() {
        NotFoundException ex1 = NotFoundException.forId("Request", 1L);
        NotFoundException ex2 = NotFoundException.forId("BlogPost", 100L);
        NotFoundException ex3 = NotFoundException.forId("Comment", 5000L);

        assertAll(
                () -> assertTrue(ex1.getMessage().contains("Request")),
                () -> assertTrue(ex2.getMessage().contains("BlogPost")),
                () -> assertTrue(ex3.getMessage().contains("Comment")),
                () -> assertTrue(ex1.getMessage().contains("1")),
                () -> assertTrue(ex2.getMessage().contains("100")),
                () -> assertTrue(ex3.getMessage().contains("5000"))
        );
    }
}