package dev.appianway.dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

// Configuration class for CORS settings
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${frontend.allowed-origins}")
    private String frontendOrigin;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to API endpoints
                .allowedOrigins(frontendOrigin) // Frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}