package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public record IncomeEditDto(
    @Schema (description = "수입 수정 금액")
    Integer amount,
    @Schema (description = "수입 메모")
    String note,
    @Schema (description = "수입 날짜")
    LocalDate date
    ) {

}

