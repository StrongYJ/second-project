package com.secondproject.monthlycoffee.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteExpenseDto(
    @Schema(description = "삭제된 지출 식별 번호")
    Long id,
    @Schema(description = "삭제 메세지")
    String message
) {
    
}
