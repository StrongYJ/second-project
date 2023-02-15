package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record IncomeNewDto(
    @Schema (description = "수입 금액")
    Integer amount,
    @Schema (description = "수입에 대한 메모")
    String note,
    @Schema (description = "수입 날짜")
    LocalDate date,
    @Schema (description = "회원 정보")
    MemberDto member
    ) {
        public IncomeNewDto(IncomeInfo entity) {
            this(
                entity.getAmount(),
                entity.getNote(),
                entity.getDate(),
                new MemberDto(entity.getMember()) 
                );
        }
}
