package com.secondproject.monthlycoffee.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;

public record IncomeDeleteDto(
    @Schema (description = "회원 식별 번호")
    Long id,
    @Schema (description = "수입 삭제 메시지")
    String message
    ) {
        
}
