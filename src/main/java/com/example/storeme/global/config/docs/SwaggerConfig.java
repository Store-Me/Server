package com.example.storeme.global.config.docs;

import com.example.storeme.global.config.properties.JwtProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Store Me",
                description = "Store Me API 명세서",
                version = "v1"),
        servers = {
                @Server(url = "https://storeme.shop"),
                @Server(url = "http://localhost:8080")
        }
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("Store Me API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI getOpenApi() {
        Components components = new Components()
                .addSecuritySchemes("Access Token (Bearer)", getJwtSecurityScheme())
                .addSecuritySchemes("Refresh Token", getJwtRefreshSecurityScheme());

        SecurityRequirement securityItem = new SecurityRequirement()
                .addList("Access Token (Bearer)")
                .addList("Refresh Token");

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityItem);
    }
    private SecurityScheme getJwtSecurityScheme() {
        return new SecurityScheme()
                .name(jwtProperties.getAccessHeader())
                .scheme(jwtProperties.getBearer())
                .bearerFormat("JWT")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);
    }

    private SecurityScheme getJwtRefreshSecurityScheme() {
        return new SecurityScheme()
                .name(jwtProperties.getRefreshHeader())
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);
    }
}
