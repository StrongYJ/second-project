package com.secondproject.monthlycoffee.dto.budget;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

// public class BudgetListResponseDto {
//     private List<BudgetDto> budgetList;
//     private Long total;
//     private Integer totalPage;
//     private Integer currentPage;
// }

public record BudgetListResponseDto(
    @Schema(description = "예산 리스트")
    List<BudgetDto> budgetList,
    @Schema(description = "총 예산 갯수")
    Long total,
    @Schema(description = "총 페이지 수")
    Integer totalPage,
    @Schema(description = "조회한 페이지")
    Integer currentPage
    ) {

}
