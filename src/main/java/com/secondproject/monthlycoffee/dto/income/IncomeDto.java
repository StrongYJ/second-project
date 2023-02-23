package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record IncomeDto(
    @Schema (description = "수입 식별 번호")
    Long id,
    @Schema (description = "수입 설정 금액")
    Integer amount,
    @Schema (description = "수입 메모")
    String note,
    @Schema (description = "수입 날짜")
    LocalDate date
    ) {
    public IncomeDto(IncomeInfo entity) {
        this(
            entity.getId(), 
            entity.getAmount(), 
            entity.getNote(),
            entity.getDate()
        );
    }
    
}
