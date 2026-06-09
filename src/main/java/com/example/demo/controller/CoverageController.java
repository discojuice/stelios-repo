// package com.example.demo.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import lombok.Getter;
// import lombok.Setter;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.Date;
// import org.springframework.core.io.FileSystemResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;

// import java.io.File;

// /**
//  * Controller for serving JaCoCo code coverage reports.
//  */
// @RestController
// @RequestMapping("/api/coverage")
// @CrossOrigin(
//         origins = {
//                 "http://localhost:4200",
//                 "http://localhost:3000",
//                 "https://myproject-1-vf3w.onrender.com"
//         },
//         methods = {RequestMethod.GET, RequestMethod.OPTIONS},
//         allowedHeaders = "*",
//         allowCredentials = "true"
// )
// public class CoverageController {

//     private static final String JACOCO_REPORT_PATH = "target/site/jacoco/index.html";

//     /**
//      * Health check endpoint
//      */
//     @GetMapping("/health")
//     public ResponseEntity<String> health() {
//         return ResponseEntity.ok("{\"status\": \"Coverage API is running\"}");
//     }

//     /**
//      * Returns information about coverage status
//      */
//     @GetMapping("/status")
//     public ResponseEntity<CoverageStatus> getCoverageStatus() {
//         try {
//             Path reportPath = Paths.get(JACOCO_REPORT_PATH);
            
//             boolean exists = Files.exists(reportPath);
//             long lastModified = exists ? Files.getLastModifiedTime(reportPath).toMillis() : 0;

//             CoverageStatus status = new CoverageStatus(
//                     exists,
//                     lastModified,
//                     exists ? 
//                         "✓ JaCoCo report available. Last generated: " + new Date(lastModified) :
//                         "⚠ JaCoCo report not generated yet. Run: mvn clean test"
//             );

//             return ResponseEntity.ok(status);
//         } catch (IOException e) {
//             return ResponseEntity.ok(new CoverageStatus(
//                     false, 
//                     0, 
//                     "Error reading report: " + e.getMessage()
//             ));
//         }
//     }


//     @GetMapping(value = "/report", produces = MediaType.TEXT_HTML_VALUE)
//     public ResponseEntity<Resource> getJacocoReport() {

//         File report = new File("target/site/jacoco/index.html");

//         if (!report.exists()) {
//             return ResponseEntity.notFound().build();
//         }

//         return ResponseEntity.ok()
//                 .contentType(MediaType.TEXT_HTML)
//                 .body(new FileSystemResource(report));
//     }

//     /**
//      * DTO for coverage status response
//      */
//     @Setter
//     @Getter
//     public static class CoverageStatus {
//         private boolean available;
//         private long lastModified;
//         private String message;

//         public CoverageStatus(boolean available, long lastModified, String message) {
//             this.available = available;
//             this.lastModified = lastModified;
//             this.message = message;
//         }
//     }
// }