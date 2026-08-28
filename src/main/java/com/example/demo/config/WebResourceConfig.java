package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

/**
 * Configuration for serving static resources like JaCoCo coverage reports.
 */
@Configuration
public class WebResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get the absolute path to target/site/jacoco
        String jacocoPath = Paths.get("target/site/jacoco").toAbsolutePath().toString();

        // Serve JaCoCo coverage reports from target/site/jacoco
        registry.addResourceHandler("/coverage/**")
                .addResourceLocations("file:" + jacocoPath + "/");
    }
}