package com.secondproject.monthlycoffee.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageExpenseDto(
    @Schema(description = "식별 번호")
    Long id,
    @Schema(description = "메세지")
    String message
) {
    
}
