package com.secondproject.monthlycoffee.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;

public record IncomeMessageDto(
    @Schema (description = "회원 식별 번호")
    Long id,
    @Schema (description = "메시지")
    String message
    ) {
        
}
