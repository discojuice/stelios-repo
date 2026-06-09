package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(mockRequest.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");

        ResponseEntity<ApiError> response = exceptionHandler.handleNotFound(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("Resource not found", response.getBody().message());
        assertEquals("/api/test", response.getBody().path());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void testHandleBadRequestException() {
        BadRequestException ex = new BadRequestException("Invalid request data");

        ResponseEntity<ApiError> response = exceptionHandler.handleBadRequest(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("Invalid request data", response.getBody().message());
        assertEquals("/api/test", response.getBody().path());
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() {
        MethodArgumentTypeMismatchException ex = 
                new MethodArgumentTypeMismatchException("invalidValue", String.class, "id", null, null);

        ResponseEntity<ApiError> response = exceptionHandler.handleMethodArgumentTypeMismatch(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertTrue(response.getBody().message().contains("Invalid argument"));
    }

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal value provided");

        ResponseEntity<ApiError> response = exceptionHandler.handleIllegalArgument(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Illegal value provided", response.getBody().message());
    }

    @Test
    void testHandleNoHandlerFound() throws NoHandlerFoundException {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/notfound", null);

        ResponseEntity<ApiError> response = exceptionHandler.handleNoHandlerFound(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertTrue(response.getBody().message().contains("Endpoint not found"));
    }

    @Test
    void testHandleAnyException() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ApiError> response = exceptionHandler.handleAny(ex, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertEquals("/api/test", response.getBody().path());
    }

    @Test
    void testApiErrorTimestamp() {
        NotFoundException ex = new NotFoundException("Test");
        Instant beforeCall = Instant.now();

        ResponseEntity<ApiError> response = exceptionHandler.handleNotFound(ex, mockRequest);

        Instant afterCall = Instant.now();
        Instant timestamp = response.getBody().timestamp();

        assertNotNull(timestamp);
        assertTrue(timestamp.isAfter(beforeCall.minusSeconds(1)));
        assertTrue(timestamp.isBefore(afterCall.plusSeconds(1)));
    }

    @Test
    void testMultipleExceptionCalls() {
        BadRequestException ex1 = new BadRequestException("First error");
        NotFoundException ex2 = new NotFoundException("Second error");

        ResponseEntity<ApiError> response1 = exceptionHandler.handleBadRequest(ex1, mockRequest);
        ResponseEntity<ApiError> response2 = exceptionHandler.handleNotFound(ex2, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals("First error", response1.getBody().message());
        assertEquals("Second error", response2.getBody().message());
    }

    @Test
    void testHandleNotFoundWithDifferentPaths() {
        when(mockRequest.getRequestURI()).thenReturn("/api/requests/1");
        NotFoundException ex = new NotFoundException("Request not found");

        ResponseEntity<ApiError> response = exceptionHandler.handleNotFound(ex, mockRequest);

        assertEquals("/api/requests/1", response.getBody().path());
    }

    @Test
    void testHandleExceptionWithLongMessage() {
        String longMessage = "This is a very long error message that contains detailed information about what went wrong";
        BadRequestException ex = new BadRequestException(longMessage);

        ResponseEntity<ApiError> response = exceptionHandler.handleBadRequest(ex, mockRequest);

        assertEquals(longMessage, response.getBody().message());
    }
}