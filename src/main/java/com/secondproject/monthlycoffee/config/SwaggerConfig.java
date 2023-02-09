package com.secondproject.monthlycoffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI floOpenAPI() {
        Info info = new Info().version("0.0.1").title("두번째 프로젝트 Monthly Coffee").description("커피매니아를 위한 가계부");
        return new OpenAPI().info(info);
    }
}
