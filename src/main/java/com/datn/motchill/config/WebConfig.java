package com.datn.motchill.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả API
                        .allowedOrigins("http://localhost:4200") // URL Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                        .allowedHeaders("*")
                        .exposedHeaders("Content-Type", "Content-Length", "Content-Range", "Accept-Ranges")
                        .maxAge(3600) // Cache CORS preflight requests
                        .allowCredentials(true);
            }
        };
    }
}
