package com.course.courseapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${mbartosz.openapi.dev-url}")
    private String devUrl;

    @Value("${mbartosz.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI courseManagementOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .description("REST API for managing courses")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("you@example.com")
                                .url("https://www.bartosz.com")))
                .servers(List.of(devServer, prodServer));
    }
}