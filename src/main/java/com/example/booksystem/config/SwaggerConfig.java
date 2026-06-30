package com.example.booksystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookSystemAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Book Management System API")
                        .description("REST APIs for Book Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Pradeep Kalla")
                                .email("pradeep@example.com")));
    }
}