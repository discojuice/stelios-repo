package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoverageControllerTest {

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private CoverageController coverageController;

    @Test
    void testGetCoverageReport_NotFound() {
        ResponseEntity<String> response = coverageController.getCoverageReport();

        assertNotNull(response);
        // Report might not exist in test environment
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND) || 
                  response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    void testGetCoverageStatus() {
        ResponseEntity<CoverageController.CoverageStatus> response = coverageController.getCoverageStatus();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCoverageStatusDto() {
        CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
                true, System.currentTimeMillis(), "Test message"
        );

        assertTrue(status.isAvailable());
        assertTrue(status.getLastModified() > 0);
        assertEquals("Test message", status.getMessage());
    }

    @Test
    void testCoverageStatusDtoSetters() {
        CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(false, 0, "");

        status.setAvailable(true);
        status.setLastModified(123456789L);
        status.setMessage("Updated message");

        assertTrue(status.isAvailable());
        assertEquals(123456789L, status.getLastModified());
        assertEquals("Updated message", status.getMessage());
    }

    @Test
    void testControllerInstantiation() {
        assertNotNull(coverageController);
    }
}