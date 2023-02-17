package com.secondproject.monthlycoffee.config.security;

import java.util.Date;
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
    
    private final String key;

    public JwtUtil(@Value("${jwt.key}") final String key) {
        this.key = key;
    }

    public String createAccess(final long memberId) {
        return JwtProperties.TOKEN_PREFIX + 
            JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("memberId", memberId)
                .sign(Algorithm.HMAC256(key));
    }

    public Long verifyAndExtractClaim(final String token) {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token).getClaim("memberId").asLong();
    }

    public String resolve(final String token) {
        return token.replace(JwtProperties.TOKEN_PREFIX, "");
    }

    public Authentication getAuthentication(final long memberId) {
        return new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
