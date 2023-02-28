package com.secondproject.monthlycoffee.strongyj.security;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;

import io.restassured.RestAssured;
import io.restassured.http.Header;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class JwtReissueTest {

    @Autowired 
    private JwtUtil jwtUtil;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 만료된_액세스_토큰() throws InterruptedException {
        final long memberId = 1L;

        var token = jwtUtil.createAccess(memberId, 0);

        Thread.sleep(100L);
        
        given().log().all()
            .header(new Header(HttpHeaders.AUTHORIZATION, token))
        .when()
            .post("/test")
        .then().log().all()
                .statusCode(401)
                .assertThat().body("error", containsString("TokenExpiredException"));
    }

    @Test
    void 만료되지않은_액세스_토큰() throws InterruptedException {
        final long memberId = 1L;

        var token = jwtUtil.createAccess(memberId, 10000000L);
        
        String response = given().log().all()
            .header(new Header(HttpHeaders.AUTHORIZATION, token))
        .when()
            .post("/test")
        .then().log().all()
        .extract().asString();

        Assertions.assertThat(response).isEqualTo("test" + memberId);
    }

    @Test
    void 잘못된_액세스_토큰() throws InterruptedException {
        final long memberId = 1L;

        var token = JwtProperties.ACCESS_TOKEN_PREFIX +
                JWT.create()
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10100000L))
                        .withClaim("memberId", memberId)
                        .sign(Algorithm.HMAC256("key"));
        
        given().log().all()
            .header(new Header(HttpHeaders.AUTHORIZATION, token))
        .when()
            .post("/test")
        .then().log().all()
        .statusCode(401)
        .body("error", containsString("JWTVerificationException"));

    }

    @Test
    void 토큰없이_접근() throws InterruptedException {
        
        given().log().all()
        .when()
            .post("/test")
        .then().log().all()
        .statusCode(401)
        .body("message", containsString("needs login"));
    }
}
