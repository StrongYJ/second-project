package com.secondproject.monthlycoffee.dto.member;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import com.secondproject.monthlycoffee.entity.type.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LoginResponseDto(
        @Schema(description = "회원 식별 번호")
        Long id,
        @Schema(description = "회원 인증 도메인")
        AuthDomain authDomain,
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "생일")
        LocalDate birth,
        @Schema(description = "성별")
        Gender gender,
        @Schema(description = "액세스 토큰 유효시간 (밀리초)")
        Long accessExpirationTime
) {
    public LoginResponseDto(MemberDto memberDto) {
        this(memberDto.id(), memberDto.authDomain(), memberDto.nickname(), memberDto.birth(), memberDto.gender(), JwtProperties.ACCESS_EXPIRATION_TIME);
    }
}
