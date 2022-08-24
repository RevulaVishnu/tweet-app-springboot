package com.cts.apigatway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {

    @Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tweet-service", r-> r.path("/api/v1.0/tweets/**").uri("http://localhost:8082/"))
                .route("user-service", r->r.path("/api/v1.0/user/**").uri("http://localhost:8083/"))
                .route("AUTH-SERVICE", r->r.path("/api/v1.0/login").uri("http://localhost:8081/"))
                .build();
    }
}
