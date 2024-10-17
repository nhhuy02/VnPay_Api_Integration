package com.nhhgrp.customer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI orderServiceAPI(){
        Parameter parameter = new Parameter().name("lang").description("I18n").required(Boolean.FALSE);
        return new OpenAPI()
                .info(new Info().title("Customer Service API")
                        .description("This is the REST API for Customer Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .components(new Components().parameters(
                        Map.of("param",parameter)
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer to Customer Service Wiki Documentation")
                        .url("https://order-customer-dummy-url.com/docs"));
    }
}

