package com.secondproject.monthlycoffee.dto.budget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BudgetListDto {
    @Schema (description = "예산 식별 번호")
    Long id;
    @Schema (description = "예산 설정 금액")
    Integer amount;
    @Schema (description = "예산 설정 월")
    String month;
}
