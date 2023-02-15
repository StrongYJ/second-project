package com.secondproject.monthlycoffee.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateExpenseDto(
    @Schema(description = "지출 식별 번호", example = "1")
    Long id,
    @Schema(description = "메세지", example = "등록되었습니다.")
    String message
) {
    
}
