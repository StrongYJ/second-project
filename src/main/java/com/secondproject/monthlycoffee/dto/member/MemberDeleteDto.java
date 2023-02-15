package com.secondproject.monthlycoffee.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberDeleteDto(
    @Schema (description = "회원 식별 번호")
    Long id,
    @Schema (description = "탈퇴 메시지")
    String message
    ) {
        
}
