package com.example.CoopCredit.infrastructure.config.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Definimos el nombre del esquema de seguridad que se usará en la UI de Swagger
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // Añadimos la definición del esquema de seguridad a los componentes de OpenAPI
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP) // Tipo de seguridad: HTTP
                                                .scheme("bearer")               // Esquema: Bearer Token
                                                .bearerFormat("JWT")            // Formato del token: JWT
                                )
                )
                // Añadimos el requisito de seguridad a nivel global para todas las operaciones
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Opcional: Añadimos información general sobre nuestra API
                .info(new Info().title("API de CoopCredit").version("v1"));
    }
}