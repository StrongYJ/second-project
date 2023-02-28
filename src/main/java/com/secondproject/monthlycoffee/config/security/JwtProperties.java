package com.secondproject.monthlycoffee.config.security;

public interface JwtProperties {
    // acesss
    long ACCESS_EXPIRATION_TIME = 1000 * 60 * 5;
    String ACCESS_TOKEN_PREFIX = "Bearer ";
    
    // refresh
    long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    String REISSUE_TOKEN_URI = "/api/reissue-token";
    String REFRESH_HEADER_NAME = "RefreshToken";

    String INVALID_TOKEN_MESSAGE = "Invalid Token";
}
