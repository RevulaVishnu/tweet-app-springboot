package com.cts.apigatway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
public class SpringCloudGatewayRouting {

    @Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tweet-service", r-> r.path("/api/v1.0/tweets/**").uri("http://tweet-service:8082/"))
                .route("user-service", r->r.path("/api/v1.0/user/**").uri("http://user-service:8083/"))
                .route("AUTH-SERVICE", r->r.path("/api/v1.0/login","/api/v1.0/validate").uri("http://auth-service:8081/"))
                .build();
    }

}
