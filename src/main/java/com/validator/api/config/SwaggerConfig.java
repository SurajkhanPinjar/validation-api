package com.validator.api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDetails() {
        return new OpenAPI()
                .info(new Info()
                        .title("Advanced Phone & Email Validator API")
                        .description("Real-time validation of Email, Phone, IP, and ZIP codes with MX lookup, phone type detection, and risk scoring.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Validator API Support")
                                .email("support@validatorapi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("API Docs")
                        .url("https://swagger.io/"));
    }
}