package com.secondproject.monthlycoffee.strongyj.api;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;
import com.secondproject.monthlycoffee.strongyj.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;


@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class CommentAPIControllerTest extends ApiTest {

    @Autowired private JwtUtil jwtUtil;

    @Test
    void 댓글_삭제() {
        String access = jwtUtil.createAccess(1L, JwtProperties.ACCESS_EXPIRATION_TIME);
        given()
                .header(HttpHeaders.AUTHORIZATION, access)
        .when()
                .delete("/api/comments/1")
        .then().log().all()
                .statusCode(200);
    }
}
