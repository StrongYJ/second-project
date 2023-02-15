package com.secondproject.monthlycoffee.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberEditDto(
    @Schema (description = "회원 수정 닉네임")
    String nickname
    ) {

}