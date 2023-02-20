package com.secondproject.monthlycoffee.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;

public interface IncomeAvgDto {
    @Schema (description = "조회하려는 연월 [ex)2023-01]")
    String getYearMonth();
    @Schema (description = "수입 평균")
    Integer getAvg();
}
