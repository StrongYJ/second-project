package com.secondproject.monthlycoffee.dto.budget;

import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record BudgetNewDto(
    
    @Schema (description = "예산 설정 금액")
    Integer amount
    ) {
        public BudgetNewDto(BudgetInfo entity) {
            this(entity.getAmount());
        }
}
