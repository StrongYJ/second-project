package com.secondproject.monthlycoffee.config.security;

public interface JwtProperties {

    long ACCESS_EXPIRATION_TIME = 1000 * 10;
    long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    String TOKEN_PREFIX = "Bearer ";
    
}
