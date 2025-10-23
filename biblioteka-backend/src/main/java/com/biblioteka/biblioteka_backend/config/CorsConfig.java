// src/main/java/com/biblioteka/biblioteka_backend/config/CorsConfig.java
package com.biblioteka.biblioteka_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                        .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS","HEAD")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}


