package com.secondproject.monthlycoffee.strongyj.api;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.dto.member.LoginResponseDto;
import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.dto.member.MemberLoginDto;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class MemberAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired private MemberInfoRepository memberRepo;
    private final static String[] nickname = {"커피매니아", "커피맨이야", "커피Mania", "커피덕후", "커피짱짱", "커피사랑나라사랑", "커피러버", "커피Lover"};

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void noRegMember_NullNickname() {
        String uid = UUID.randomUUID().toString();
        MemberLoginDto memberLoginDto = new MemberLoginDto(AuthDomain.KAKAO, uid, null);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(memberLoginDto)
                .when()
                    .post("/api/members")
                .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                .   extract();
        Assertions.assertThat(response.header(HttpHeaders.AUTHORIZATION)).isNotBlank();
        Assertions.assertThat(response.header(JwtProperties.REFRESH_HEADER_NAME)).isNotBlank();
        LoginResponseDto newMember = response.body().as(LoginResponseDto.class);
        Assertions.assertThat(newMember.nickname()).isIn(Arrays.stream(nickname).toList());
        Assertions.assertThat(newMember.accessExpirationTime()).isEqualTo(JwtProperties.ACCESS_EXPIRATION_TIME);
    }

    @Test
    void noRegMember_NotNullNickname() {
        String uid = UUID.randomUUID().toString();
        MemberLoginDto memberLoginDto = new MemberLoginDto(AuthDomain.KAKAO, uid, "커피테스트");

        ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(memberLoginDto)
                .when()
                .post("/api/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        Assertions.assertThat(response.header(HttpHeaders.AUTHORIZATION)).isNotBlank();
        Assertions.assertThat(response.header(JwtProperties.REFRESH_HEADER_NAME)).isNotBlank();
        LoginResponseDto newMember = response.body().as(LoginResponseDto.class);
        Assertions.assertThat(newMember.nickname()).isEqualTo("커피테스트");
        Assertions.assertThat(newMember.accessExpirationTime()).isEqualTo(JwtProperties.ACCESS_EXPIRATION_TIME);
    }

    @Test
    void regMember_NullNickname() {
        MemberInfo memberInfo = new MemberInfo(AuthDomain.KAKAO, UUID.randomUUID().toString(), null, null, null);
        memberRepo.save(memberInfo);
        MemberLoginDto memberLoginDto = new MemberLoginDto(memberInfo.getAuthDomain(), memberInfo.getUid(), null);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(memberLoginDto)
                .when()
                .post("/api/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        LoginResponseDto member = response.body().as(LoginResponseDto.class);
        Assertions.assertThat(member.id()).isEqualTo(memberInfo.getId());
        Assertions.assertThat(member.nickname()).isIn(Arrays.stream(nickname).toList());
        Assertions.assertThat(response.header(HttpHeaders.AUTHORIZATION)).isNotBlank();
        Assertions.assertThat(response.header(JwtProperties.REFRESH_HEADER_NAME)).isNotBlank();
        Assertions.assertThat(member.accessExpirationTime()).isEqualTo(JwtProperties.ACCESS_EXPIRATION_TIME);
    }

    @Test
    void regMember_NotNullNickname() {
        MemberInfo memberInfoNotnull = new MemberInfo(AuthDomain.KAKAO, UUID.randomUUID().toString(), "커피테스트", null, null);
        memberRepo.save(memberInfoNotnull);

        MemberLoginDto memberLoginDto = new MemberLoginDto(memberInfoNotnull.getAuthDomain(), memberInfoNotnull.getUid(), null);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(memberLoginDto)
                .when()
                .post("/api/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        LoginResponseDto member = response.body().as(LoginResponseDto.class);
        Assertions.assertThat(member.id()).isEqualTo(memberInfoNotnull.getId());
        Assertions.assertThat(member.nickname()).isEqualTo(memberInfoNotnull.getNickname());
        Assertions.assertThat(response.header(HttpHeaders.AUTHORIZATION)).isNotBlank();
        Assertions.assertThat(response.header(JwtProperties.REFRESH_HEADER_NAME)).isNotBlank();
        Assertions.assertThat(member.accessExpirationTime()).isEqualTo(JwtProperties.ACCESS_EXPIRATION_TIME);
    }
}
