package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.dto.member.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IncomeStringDateDto {
    @Schema (description = "수입 식별 번호")
    Long id;
    @Schema (description = "수입 금액")
    Integer amount;
    @Schema (description = "수입에 대한 메모")
    String note;
    @Schema (description = "수입 날짜")
    String date;
    @Schema (description = "회원 정보")
    MemberDto member;
}
