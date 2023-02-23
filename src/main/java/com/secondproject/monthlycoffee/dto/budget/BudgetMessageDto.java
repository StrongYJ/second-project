package com.secondproject.monthlycoffee.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetMessageDto(
    @Schema (description = "회원 식별 번호")
    Long id,
    @Schema (description = "메시지")
    String message
    ) {
}
