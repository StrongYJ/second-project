package com.secondproject.monthlycoffee.dto.member;

import com.secondproject.monthlycoffee.entity.MemberInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberDto(
    @Schema(description = "회원 식별 번호")
    Long id,
    @Schema(description = "닉네임")
    String nickname
) {
    public MemberDto(MemberInfo entity) {
        this(entity.getId(), entity.getNickname());
    }    
}
