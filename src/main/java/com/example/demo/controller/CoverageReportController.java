package com.example.demo.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller for serving the JaCoCo coverage report HTML file.
 */
@RestController
@RequestMapping("/api/coverage-report")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://localhost:3000",
                "https://myproject-1-vf3w.onrender.com"
        },
        methods = {RequestMethod.GET, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class CoverageReportController {

    private static final String JACOCO_REPORT_PATH = "target/site/jacoco/index.html";

    /**
     * Returns the JaCoCo coverage report as a file resource
     */
    @GetMapping
    public ResponseEntity<Resource> getCoverageReport() {
        try {
            Path reportPath = Paths.get(JACOCO_REPORT_PATH).toAbsolutePath();
            
            if (!Files.exists(reportPath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            Resource resource = new FileSystemResource(reportPath);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"index.html\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    /**
     * Returns the raw HTML as string (for iframe loading)
     */
    @GetMapping("/html")
    public ResponseEntity<String> getCoverageReportHtml() {
        try {
            Path reportPath = Paths.get(JACOCO_REPORT_PATH).toAbsolutePath();
            
            if (!Files.exists(reportPath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            String htmlContent = Files.readString(reportPath);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                    .body(htmlContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}