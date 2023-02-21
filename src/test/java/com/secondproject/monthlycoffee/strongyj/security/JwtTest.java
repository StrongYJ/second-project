package com.secondproject.monthlycoffee.strongyj.security;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

@ActiveProfiles("test")
public class JwtTest {
    
    
    @Test
    void 만료된_토큰() throws InterruptedException {
        final String secret = "secret";
        final int testId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        String jwt = JWT.create().withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + 1L)).withClaim("id", testId).sign(Algorithm.HMAC256(secret));
        
        Thread.sleep(100L);

        Integer check = null;
        try {
            check = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt).getClaim("id").asInt();
        } catch (TokenExpiredException e) {
            check = JWT.decode(jwt).getClaim("id").asInt();
        }

        Assertions.assertThat(check).isEqualTo(testId);
    }

    @Test
    void 정상적인_토큰() throws InterruptedException {
        final String secret = "secret";
        final int testId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        String jwt = JWT.create().withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + 1000L)).withClaim("id", testId).sign(Algorithm.HMAC256(secret));
        
        Integer check = null;
        try {
            check = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt).getClaim("id").asInt();
        } catch (TokenExpiredException e) {
            check = JWT.decode(jwt).getClaim("id").asInt();
        }

        Assertions.assertThat(check).isEqualTo(check);
    }

    @Test
    void 만료된_키가_맞지않는_토큰() throws InterruptedException {
        
        Assertions.assertThatThrownBy(() -> {
            final String secret = "secret";
            final int testId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            String jwt = JWT.create().withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + 1L))
                    .withClaim("id", testId).sign(Algorithm.HMAC256("not secret"));


            Thread.sleep(100L);
            
            Integer check = null;
            try {
                check = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt).getClaim("id").asInt();
            } catch (TokenExpiredException e) {
                check = JWT.decode(jwt).getClaim("id").asInt();
            }
            Assertions.assertThat(check).isEqualTo(null);
        }).isInstanceOf(SignatureVerificationException.class);
    }

    @Test
    void 만료된_알고리즘이_맞지않는_토큰() throws InterruptedException {
        Assertions.assertThatThrownBy(() -> {
            final String secret = "secret";
            final int testId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            String jwt = JWT.create().withExpiresAt(Instant.ofEpochMilli(Instant.now().toEpochMilli() + 1L))
                    .withClaim("id", testId).sign(Algorithm.HMAC512(secret));

            Thread.sleep(100L);

            Integer check = null;
            try {
                check = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt).getClaim("id").asInt();
            } catch (TokenExpiredException e) {
                check = JWT.decode(jwt).getClaim("id").asInt();
            }
            Assertions.assertThat(check).isEqualTo(null);
        }).isInstanceOf(AlgorithmMismatchException.class);
    }
    
}
