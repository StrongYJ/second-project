package com.secondproject.monthlycoffee.dto.member;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import com.secondproject.monthlycoffee.entity.type.Gender;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberLoginDto(
        @Schema(description = "가입 도메인 이름", example = "KAKAO")
        AuthDomain authDomain,
    @Schema(description = "회원 UID")
    String uid,
        @Schema(description = "회원 닉네임", nullable = true)
        String nickname
) {
    public MemberLoginDto(MemberInfo entity) {
        this(
                entity.getAuthDomain(),
            entity.getUid(),
                entity.getNickname()
            );
    }

    public MemberInfo toEntity() {
        return MemberInfo.builder()
                .authDomain(authDomain)
                .uid(uid)
                .nickname(nickname)
                .build();
    }

}