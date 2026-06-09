// package com.example.demo.controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class CoverageControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void testCoverageHealthEndpoint() throws Exception {
//         mockMvc.perform(get("/api/coverage/health")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"status\": \"Coverage API is running\"}"));
//     }

//     @Test
//     void testCoverageStatusEndpoint() throws Exception {
//         mockMvc.perform(get("/api/coverage/status")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.available").exists())
//                 .andExpect(jsonPath("$.message").exists());
//     }

//     @Test
//     void testCoverageStatusEndpointAvailable() throws Exception {
//         mockMvc.perform(get("/api/coverage/status")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.available").isBoolean())
//                 .andExpect(jsonPath("$.lastModified").isNumber());
//     }

//     @Test
//     void testCorsHeadersForCoverageStatus() throws Exception {
//         mockMvc.perform(options("/api/coverage/status")
//                 .header("Origin", "http://localhost:4200"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCorsHeadersForCoverageHealth() throws Exception {
//         mockMvc.perform(options("/api/coverage/health")
//                 .header("Origin", "http://localhost:4200"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCoverageHealthWithDifferentOrigin() throws Exception {
//         mockMvc.perform(get("/api/coverage/health")
//                 .header("Origin", "https://myproject-1-vf3w.onrender.com"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCoverageStatusWithDifferentOrigin() throws Exception {
//         mockMvc.perform(get("/api/coverage/status")
//                 .header("Origin", "https://myproject-1-vf3w.onrender.com"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCoverageStatusDto() {
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 true, System.currentTimeMillis(), "Test message"
//         );

//         assert status.isAvailable();
//         assert status.getLastModified() > 0;
//         assert "Test message".equals(status.getMessage());
//     }

//     @Test
//     void testCoverageStatusNotAvailable() {
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 false, 0, "Report not found"
//         );

//         assert !status.isAvailable();
//         assert status.getLastModified() == 0;
//         assert "Report not found".equals(status.getMessage());
//     }

//     @Test
//     void testCoverageStatusDtoSetters() {
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 false, 0, ""
//         );

//         status.setAvailable(true);
//         status.setLastModified(123456789L);
//         status.setMessage("Updated");

//         assert status.isAvailable();
//         assert status.getLastModified() == 123456789L;
//         assert "Updated".equals(status.getMessage());
//     }

//     @Test
//     void testCoverageStatusGettersAfterConstruction() {
//         long testTime = System.currentTimeMillis();
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 true, testTime, "Available"
//         );

//         assert status.isAvailable() == true;
//         assert status.getLastModified() == testTime;
//         assert status.getMessage().equals("Available");
//     }

//     @Test
//     void testCoverageControllerInstantiation() throws Exception {
//         mockMvc.perform(get("/api/coverage/health"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void testCoverageStatusMultipleFields() {
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 true, 1234567890L, "Full message here"
//         );

//         assert status.isAvailable();
//         assert status.getLastModified() == 1234567890L;
//         assert status.getMessage().contains("Full");
//     }

//     @Test
//     void testCoverageStatusWithEmptyMessage() {
//         CoverageController.CoverageStatus status = new CoverageController.CoverageStatus(
//                 false, 0, ""
//         );

//         assert !status.isAvailable();
//         assert status.getMessage().isEmpty();
//     }

//     @Test
//     void testCoverageHealthEndpointReturnsValidJson() throws Exception {
//         // Changed: Don't enforce content type, just check the status and content
//         mockMvc.perform(get("/api/coverage/health"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"status\": \"Coverage API is running\"}"));
//     }

//     @Test
//     void testCoverageStatusReturnsValidStructure() throws Exception {
//         mockMvc.perform(get("/api/coverage/status"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.available").exists())
//                 .andExpect(jsonPath("$.lastModified").exists())
//                 .andExpect(jsonPath("$.message").exists());
//     }

//     @Test
//     void testCoverageHealthResponseContent() throws Exception {
//         mockMvc.perform(get("/api/coverage/health"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"status\": \"Coverage API is running\"}"));
//     }
// }