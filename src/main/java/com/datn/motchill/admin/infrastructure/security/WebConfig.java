package com.datn.motchill.admin.infrastructure.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "OPTIONS") // Allow OPTIONS for preflight
                .allowedHeaders("*") // Allow all headers, including Content-Type
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}
