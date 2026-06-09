package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CoverageReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCoverageReportNotFound() throws Exception {
        // In test environment, report won't exist
        mockMvc.perform(get("/api/coverage-report"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoverageReportHtmlNotFound() throws Exception {
        mockMvc.perform(get("/api/coverage-report/html"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCorsHeadersForReport() throws Exception {
        mockMvc.perform(options("/api/coverage-report")
                .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk());
    }

    @Test
    void testCorsHeadersForReportHtml() throws Exception {
        mockMvc.perform(options("/api/coverage-report/html")
                .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk());
    }

    @Test
    void testCoverageReportWithDifferentOrigin() throws Exception {
        mockMvc.perform(get("/api/coverage-report")
                .header("Origin", "https://myproject-1-vf3w.onrender.com"))
                .andExpect(status().isNotFound()); // Not found in test, but CORS should work
    }
}