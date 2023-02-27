package com.secondproject.monthlycoffee.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI monthlyCoffeeOpenAPI() {
        Info info = new Info().version("0.0.1").title("두번째 프로젝트 Monthly Coffee").description("커피매니아를 위한 가계부");
        final String securitySchemeName = "bearerAuth";
        OpenAPI openAPI = new OpenAPI().info(info);
        openAPI.addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
        return openAPI;
    }
}
