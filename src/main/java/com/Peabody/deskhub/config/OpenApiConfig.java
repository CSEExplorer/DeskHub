package com.Peabody.deskhub.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DeskHub API",
                version = "1.0",
                description = "Seat Booking Platform for Peabody ODC",
                contact = @Contact(
                        name = "DeskHub Team",
                        email = "Aditya.Saxena@coforge.com"
                )
        )
)
public class OpenApiConfig {
}
