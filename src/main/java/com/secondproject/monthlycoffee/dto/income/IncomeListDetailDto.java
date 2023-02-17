package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IncomeListDetailDto {
    @Schema (description = "수입 식별 번호")
    Long id;
    @Schema (description = "수입 설정 금액")
    Integer amount;
    @Schema (description = "수입 메모")
    String note;
    @Schema (description = "수입 날짜")
    LocalDate date;
}
