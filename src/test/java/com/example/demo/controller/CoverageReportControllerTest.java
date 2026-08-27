package com.example.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CoverageReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final Path REPORT_DIR = Path.of("target/site/jacoco");
    private static final Path REPORT_FILE = REPORT_DIR.resolve("index.html");

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(REPORT_FILE);
    }

    @Test
    void testGetCoverageReportNotFound() throws Exception {
        Files.deleteIfExists(REPORT_FILE);

        mockMvc.perform(get("/api/coverage-report"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoverageReportHtmlNotFound() throws Exception {
        Files.deleteIfExists(REPORT_FILE);

        mockMvc.perform(get("/api/coverage-report/html"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoverageReportReturnsHtmlFileWhenExists() throws Exception {
        Files.createDirectories(REPORT_DIR);
        Files.writeString(REPORT_FILE, "<html><body>JaCoCo Report</body></html>");

        mockMvc.perform(get("/api/coverage-report"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"index.html\""))
                .andExpect(content().string(containsString("JaCoCo Report")));
    }

    @Test
    void testGetCoverageReportHtmlReturnsHtmlStringWhenExists() throws Exception {
        Files.createDirectories(REPORT_DIR);
        Files.writeString(REPORT_FILE, "<html><body>JaCoCo HTML Content</body></html>");

        mockMvc.perform(get("/api/coverage-report/html"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(containsString("JaCoCo HTML Content")));
    }

    @Test
    void testCorsHeadersForLocalhost4200() throws Exception {
        mockMvc.perform(options("/api/coverage-report")
                .header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    void testCorsHeadersForLocalhost3000() throws Exception {
        mockMvc.perform(options("/api/coverage-report")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    void testCorsHeadersForRenderFrontend() throws Exception {
        mockMvc.perform(options("/api/coverage-report")
                .header("Origin", "https://myproject-1-vf3w.onrender.com")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://myproject-1-vf3w.onrender.com"));
    }

    @Test
    void testCoverageReportWithRenderOriginWhenReportMissing() throws Exception {
        Files.deleteIfExists(REPORT_FILE);

        mockMvc.perform(get("/api/coverage-report")
                .header("Origin", "https://myproject-1-vf3w.onrender.com"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://myproject-1-vf3w.onrender.com"));
    }

    @Test
    void testCoverageReportHtmlWithRenderOriginWhenReportExists() throws Exception {
        Files.createDirectories(REPORT_DIR);
        Files.writeString(REPORT_FILE, "<html><body>Prod CORS Test</body></html>");

        mockMvc.perform(get("/api/coverage-report/html")
                .header("Origin", "https://myproject-1-vf3w.onrender.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://myproject-1-vf3w.onrender.com"))
                .andExpect(content().string(containsString("Prod CORS Test")));
    }

}