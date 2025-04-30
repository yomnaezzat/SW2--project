package com.filerepository.apigateway.config;

import com.filerepository.apigateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {
    
    private final AuthenticationFilter authenticationFilter;
    
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            // User Service Routes
            .route("user-service-public", r -> r
                .path("/api/users/login", "/api/users/register")
                .uri("lb://USER-SERVICE"))
            .route("user-service-protected", r -> r
                .path("/api/users/**")
                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                .uri("lb://USER-SERVICE"))
                
            // File Service Routes
            .route("file-service-protected", r -> r
                .path("/api/files/**")
                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                .uri("lb://FILE-SERVICE"))
                
            // Repository Service Routes
            .route("repository-service-protected", r -> r
                .path("/api/repositories/**", "/api/comments/**")
                .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                .uri("lb://REPOSITORY-SERVICE"))
                
            // OpenAPI Documentation Routes
            .route("user-service-docs", r -> r
                .path("/user-service/v3/api-docs/**")
                .uri("lb://USER-SERVICE"))
            .route("file-service-docs", r -> r
                .path("/file-service/v3/api-docs/**")
                .uri("lb://FILE-SERVICE"))
            .route("repository-service-docs", r -> r
                .path("/repository-service/v3/api-docs/**")
                .uri("lb://REPOSITORY-SERVICE"))
                
            // Swagger UI Route
            .route("swagger-ui", r -> r
                .path("/swagger-ui/**")
                .uri("lb://API-GATEWAY"))
            .build();
    }
}