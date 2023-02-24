package com.secondproject.monthlycoffee.dto.income;

import java.util.List;

import com.secondproject.monthlycoffee.dto.expense.ExpenseDetailDto;

import java.time.YearMonth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IncomeExpenseListDto {
    @Schema (description = "조회하려는 연월 [ex)2302]")
    Integer YearMonth;
    @Schema (description = "수입 리스트")
    List<IncomeListDetailDto> income;
    @Schema (description = "지출 리스트")
    List<ExpenseDetailDto> expense;
}
