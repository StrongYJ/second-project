package com.secondproject.monthlycoffee.config.security;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
    
    private final String accessKey;
    private final String refreshKey;

    public JwtUtil(
        @Value("${jwt.access-key}") final String accessKey,
        @Value("${jwt.refresh-key}") final String refreshKey
    ) {
        this.accessKey = accessKey;
        this.refreshKey = refreshKey;
    }

    public String createAccess(final long memberId, final long expirationTime) {
        return JwtProperties.ACCESS_TOKEN_PREFIX + 
            JWT.create()
                .withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + expirationTime))
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC256(accessKey));
    }

    public String createRefresh(final long expirationTime) {
        return JWT.create()
            .withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + expirationTime))
            .sign(Algorithm.HMAC512(refreshKey));
    }

    public Long verifyAccessAndExtractClaim(final String token) {
        return JWT.require(Algorithm.HMAC256(accessKey)).build().verify(token).getClaim("memberId").asLong();
    }

    public void verifyRefresh(final String token) {
        JWT.require(Algorithm.HMAC512(refreshKey)).build().verify(token);
    }

    public String resolve(final String token) {
        return token.replace(JwtProperties.ACCESS_TOKEN_PREFIX, "");
    }

    public Authentication getAuthentication(final long memberId) {
        return new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
