package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.service.BudgetService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetAPIController {
    private final BudgetService budgetService;

    // 예산 전체 리스트 조회
    @GetMapping("/{member-id}")
    @PageableAsQueryParam
    public ResponseEntity<Page<BudgetDto>> getBudgetList(
        @Parameter(description = "회원 식별 번호", example = "25") @PathVariable("member-id") Long id, 
        @Parameter(hidden = true) @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
            return new ResponseEntity<>(budgetService.budgetList(id, pageable), HttpStatus.OK);
        }
        
        
    // 예산 상세 조회
    @GetMapping("/{budget-id}")
    public ResponseEntity<BudgetDto> getBudgetDetail(
        @Parameter(description = "예산 식별 번호", example = "1") @PathVariable("budget-id") Long budget) {
            return new ResponseEntity<>(budgetService.budgetDetail(budget), HttpStatus.OK);
    }
}
