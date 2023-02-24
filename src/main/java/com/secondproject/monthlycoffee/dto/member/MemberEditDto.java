package com.secondproject.monthlycoffee.dto.member;

import com.secondproject.monthlycoffee.entity.type.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record MemberEditDto(
    @Schema (description = "회원 수정 닉네임", nullable = true)
    String nickname,
    @Schema(description = "회원 수정 생일", nullable = true)
    LocalDate birth,
    @Schema(description = "회원 수정 성별", nullable = true)
    Gender gender
    ) {

}