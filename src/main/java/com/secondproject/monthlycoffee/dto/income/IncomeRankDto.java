package com.secondproject.monthlycoffee.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;


public interface IncomeRankDto {
    @Schema (description = "수입 연월")
    String getYearMonth();
    @Schema (description = "수입 합계")
    Integer getSum();
    @Schema (description = "랭킹 순위")
    Integer getRank();

}
