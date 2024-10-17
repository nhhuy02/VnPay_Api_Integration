package com.nhhgrp.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Route {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product_service", r -> r
                        .path("/api/v1/products/**")
                        .uri("http://localhost:8050"))
                .route("product_service_swagger", r -> r
                        .path("/aggregate/product-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/product-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://localhost:8050"))

                .route("order_service", r -> r
                        .path("/api/v1/orders/**")
                        .uri("http://localhost:8070"))
                .route("order_service_swagger", r -> r
                        .path("/aggregate/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/order-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://localhost:8070"))

                .build();
    }
}