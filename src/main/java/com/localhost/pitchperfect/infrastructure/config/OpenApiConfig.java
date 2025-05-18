package com.localhost.pitchperfect.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pitchPerfectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pitch Perfect Football API")
                        .description("API documentation for the Pitch Perfect Football Application")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Pitch Perfect Team")
                                .email("support@pitchperfect.com")
                                .url("https://pitchperfect.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.pitchperfect.com")
                                .description("Production server")
                ));
    }
}
