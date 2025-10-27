package com.example.CorsConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                      // allow all endpoints
                        .allowedOrigins("http://localhost:4200") // only frontend
                        .allowedMethods("*")                     // allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                        .allowedHeaders("*")                     // allow all headers
                        .allowCredentials(true);                 // allow cookies and auth headers
            }
        };
    }
}
