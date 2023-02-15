package com.secondproject.monthlycoffee.dto.budget;

import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetNewDto(
    
    @Schema (description = "예산 설정 금액")
    Integer amount,
    @Schema (description = "예산 설정 월")
    String yearMonth,
    @Schema (description = "회원 정보")
    MemberDto member
    ) {
        public BudgetNewDto(BudgetInfo entity) {
            this(
                    entity.getAmount(), 
                    entity.getYearMonth().toString(),
                    new MemberDto(entity.getMember())
                );
        }
}
