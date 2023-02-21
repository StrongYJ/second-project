package com.secondproject.monthlycoffee.strongyj.api;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;
import com.secondproject.monthlycoffee.dto.member.MemberLoginDto;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import com.secondproject.monthlycoffee.token.RefreshToken;
import com.secondproject.monthlycoffee.token.RefreshTokenRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class TokenControllerTest {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private RefreshTokenRepository refreshTokenRepo;
    @LocalServerPort
    private int port;
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void loginToken() {
        ExtractableResponse<Response> loginResponse = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberLoginDto(AuthDomain.KAKAO, UUID.randomUUID().toString()))
                .when()
                    .post("/api/members")
                .then()
                    .extract();
        String access = loginResponse.header(HttpHeaders.AUTHORIZATION);
        String refresh = loginResponse.header(JwtProperties.REFRESH_HEADER_NAME);

        Assertions.assertDoesNotThrow(() -> {
            String resolveAccess = jwtUtil.resolve(access);
            jwtUtil.verifyAccessAndExtractClaim(resolveAccess);
        });
        Assertions.assertDoesNotThrow(() -> {
            jwtUtil.verifyRefresh(refresh);
        });
    }

    @Test
    @Order(2)
    void logoutToken() {
        // given
        ExtractableResponse<Response> loginResponse = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberLoginDto(AuthDomain.KAKAO, UUID.randomUUID().toString()))
                .when()
                .post("/api/members")
                .then()
                .extract();
        String access = loginResponse.header(HttpHeaders.AUTHORIZATION);
        String refresh = loginResponse.header(JwtProperties.REFRESH_HEADER_NAME);

        // when
        given()
                .header(HttpHeaders.AUTHORIZATION, access).header(JwtProperties.REFRESH_HEADER_NAME, refresh)
        .when()
                .post("/api/members/logout")
        .then()
                .assertThat().body("status", equalTo(true));

        // then
        given()
                .header(HttpHeaders.AUTHORIZATION, access).header(JwtProperties.REFRESH_HEADER_NAME, refresh)
        .when()
                .post("/test")
        .then()
                .statusCode(403)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .assertThat().body("status", equalTo(HttpStatus.valueOf(403).toString()))
                .assertThat().body("message", equalTo("This token is blacked"));
    }

    @Test
    @Order(3)
    void reissueToken() {
        // given
        String access = jwtUtil.createAccess(1L, 0L);
        String refresh = jwtUtil.createRefresh(10000000L);
        refreshTokenRepo.save(new RefreshToken(refresh, 1L, 10000000L));

        ExtractableResponse<Response> response =
                given()
                    .header(HttpHeaders.AUTHORIZATION, access)
                    .header(JwtProperties.REFRESH_HEADER_NAME, refresh)
                .when()
                    .post(JwtProperties.REISSUE_TOKEN_URI)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .assertThat().body("status", equalTo(true))
                    .extract();

        String reAccess = response.header(HttpHeaders.AUTHORIZATION);
        String refresh2 = response.header(JwtProperties.REFRESH_HEADER_NAME);

        Assertions.assertNotEquals(access, reAccess);

        Assertions.assertDoesNotThrow(() -> {
            jwtUtil.verifyAccessAndExtractClaim(jwtUtil.resolve(reAccess));
        });


        Assertions.assertDoesNotThrow(() -> {
            jwtUtil.verifyRefresh(refresh2);
        });

    }
}
