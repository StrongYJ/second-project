package com.secondproject.monthlycoffee.strongyj.api;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;
import com.secondproject.monthlycoffee.dto.member.MemberLoginDto;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class TokenControllerTest {

    @Autowired private JwtUtil jwtUtil;
    @LocalServerPort
    private int port;
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void loginToken() {
        //given
        ExtractableResponse<Response> loginResponse = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberLoginDto(AuthDomain.KAKAO, UUID.randomUUID().toString()))
                .when()
                    .post("/api/member")
                .then()
                    .extract();
        String access = loginResponse.header(HttpHeaders.AUTHORIZATION);
        String refresh = loginResponse.header(JwtProperties.REFRESH_HEADER_NAME);
        Assertions.assertThat(access).isNotBlank();
        Assertions.assertThat(refresh).isNotBlank();

    }
}
