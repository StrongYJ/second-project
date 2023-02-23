package com.secondproject.monthlycoffee.dto.budget;

import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetDto(
    @Schema (description = "예산 식별 번호")
    Long id,
    @Schema (description = "예산 설정 금액")
    Integer amount,
    @Schema (description = "예산 설정 월")
    String yearMonth
    ) {
    public BudgetDto(BudgetInfo entity) {
        this(
            entity.getId(), 
            entity.getAmount(), 
            entity.getYearMonth().toString()
        );
    }
}
