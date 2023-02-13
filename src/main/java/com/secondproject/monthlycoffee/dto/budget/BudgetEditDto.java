package com.secondproject.monthlycoffee.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetEditDto(
    @Schema (description = "예산 수정 금액")
    Integer amount
    ) {

}
