package com.secondproject.monthlycoffee.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.secondproject.monthlycoffee.config.security.AuthenticationArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {    

    private final AuthenticationArgumentResolver authenticationArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*"); // 화이트리스트 설정
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}
