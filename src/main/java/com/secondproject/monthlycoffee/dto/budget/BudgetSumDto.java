package com.secondproject.monthlycoffee.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;

public interface BudgetSumDto {
    @Schema (description = "예산 설정 연도")
    String getYear();
    @Schema (description = "예산 설정 합계")
    Integer getSum();
}
