package com.secondproject.monthlycoffee.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteExpenseDto(
    @Schema(description = "삭제한 지출 식별 번호", example = "1")
    Long id,
    @Schema(description = "메세지", example = "삭제되었습니다.")
    String message
) {
    
}
