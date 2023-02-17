package com.secondproject.monthlycoffee.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalExpenseDto {
    @Schema(description = "조회 회원 닉네임", example = "닉네임") private String nickName;
    @Schema(description = "기간 내 총 지출 금액", example = "56000") private Integer totalExpense;
    @Schema(description = "조회 시작 연월", example = "2302") private Integer startDate;
    @Schema(description = "조회 끝 연월", example = "2303") private Integer endDate;

}
