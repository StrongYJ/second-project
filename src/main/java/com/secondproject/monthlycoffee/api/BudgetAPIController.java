package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetEditDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetNewDto;
import com.secondproject.monthlycoffee.service.BudgetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "예산 관리", description = "예산 정보 CRUD API")
public class BudgetAPIController {
    private final BudgetService budgetService;


    // 예산 전체 리스트 조회
    @Operation(summary = "예산 전체 리스트 조회", description = "등록된 예산 정보들을 10개 단위로 보여줍니다.")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<BudgetDto>> getBudgetList(
        @Parameter(description = "회원 식별 번호", example = "25") @RequestParam("memberId") Long memberId, 
        @Parameter(hidden = true) @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
            return new ResponseEntity<>(budgetService.budgetList(memberId, pageable), HttpStatus.OK);
    }
        
        
    // 예산 상세 조회
    @Operation(summary = "예산 상세 조회", description = "등록된 예산 정보들 중 특정 예산을 조회합니다.")
    @GetMapping("/{budget-id}")
    public ResponseEntity<BudgetDto> getBudgetDetail(
        @Parameter(description = "예산 식별 번호", example = "1") @PathVariable("budget-id") Long budgetId) {
            return new ResponseEntity<>(budgetService.budgetDetail(budgetId), HttpStatus.OK);
    }
    
    

    // 예산 등록
    @Operation(summary = "예산 등록", description = "해당 회원의 예산을 등록합니다.")
    @PostMapping("")
    public ResponseEntity<BudgetDto> postBudget(
        @Parameter(description = "등록 할 예산 정보") @RequestBody BudgetNewDto data,
        @Parameter(description = "회원 식별 번호", example = "1") @RequestParam("memberId") Long memberId
        ) {
            return new ResponseEntity<>(budgetService.newBudget(data, memberId), HttpStatus.CREATED);
    }
    


    // 예산 수정
    @Operation(summary = "예산 수정", description = "등록된 예산 정보들 중 특정 예산을 수정합니다.")
    @PatchMapping("/{budget-id}")
    public ResponseEntity<BudgetDto> patchBudget(
        @Parameter(description = "예산 수정 내용") @RequestBody BudgetEditDto edit,
        @Parameter(description = "예산 식별 번호", example = "1") @PathVariable("budget-id") Long budgetId
        ) {
            return new ResponseEntity<>(budgetService.modifyBudget(edit, budgetId), HttpStatus.OK);
    }



    // 예산 연월별 합계
    


}
