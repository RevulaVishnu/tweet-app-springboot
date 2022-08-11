package com.cts.apigatway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SpringCloudGatewayRouting {

    @Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tweet-service", r->r.path("/api/v1.0/tweets/**").uri("lb://TWEET-SERVICE"))
                .route("user-service", r->r.path("/api/v1.0/user/**").uri("lb://USER-SERVICE"))//dynamic routing
                .build();
    }
}
