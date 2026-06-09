package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller for serving JaCoCo code coverage reports.
 */
@RestController
@RequestMapping("/api/coverage")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://myproject-1-vf3w.onrender.com"
        }
)
public class CoverageController {

    private final ResourceLoader resourceLoader;
    private static final String JACOCO_REPORT_PATH = "target/site/jacoco/index.html";
    private static final String JACOCO_DIR = "target/site/jacoco/";

    public CoverageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Returns the JaCoCo coverage report index.html
     */
    @GetMapping
    public ResponseEntity<String> getCoverageReport() {
        try {
            Path reportPath = Paths.get(JACOCO_REPORT_PATH);
            
            if (!Files.exists(reportPath)) {
                return ResponseEntity.status(404)
                        .body("{\"error\": \"JaCoCo report not found. Please run tests first: mvn clean test\"}");
            }

            String htmlContent = Files.readString(reportPath);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlContent);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Failed to read coverage report: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Returns information about coverage status
     */
    @GetMapping("/status")
    public ResponseEntity<CoverageStatus> getCoverageStatus() {
        try {
            Path reportPath = Paths.get(JACOCO_REPORT_PATH);
            
            boolean exists = Files.exists(reportPath);
            long lastModified = exists ? Files.getLastModifiedTime(reportPath).toMillis() : 0;

            return ResponseEntity.ok(new CoverageStatus(
                    exists,
                    lastModified,
                    exists ? "JaCoCo report available" : "JaCoCo report not generated"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(new CoverageStatus(false, 0, "Error reading report: " + e.getMessage()));
        }
    }

    /**
     * DTO for coverage status response
     */
    public static class CoverageStatus {
        private boolean available;
        private long lastModified;
        private String message;

        public CoverageStatus(boolean available, long lastModified, String message) {
            this.available = available;
            this.lastModified = lastModified;
            this.message = message;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}