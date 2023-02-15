package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.income.IncomeDeleteDto;
import com.secondproject.monthlycoffee.dto.income.IncomeDto;
import com.secondproject.monthlycoffee.dto.income.IncomeEditDto;
import com.secondproject.monthlycoffee.dto.income.IncomeNewDto;
import com.secondproject.monthlycoffee.service.IncomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
@Tag(name = "수입 관리", description = "수입 정보 CRUD API")
public class IncomeAPIController {
    private final IncomeService incomeService;


    // 수입 등록
    @Operation(summary = "수입 등록", description = "수입을 등록합니다.")
    @PostMapping("")
    public ResponseEntity<IncomeDto> postIncome(
        @Parameter(description = "등록 할 수입 정보") @RequestBody IncomeNewDto data,
        @Parameter(description = "회원 식별 번호", example = "1") @RequestParam("memberId") Long memberId
        ) {
            return new ResponseEntity<>(incomeService.newIncome(data, memberId), HttpStatus.CREATED);
    }


    // 수입 전체 조회
    @Operation(summary = "수입 전체 리스트 조회", description = "등록된 수입 정보들을 10개 단위로 보여줍니다.")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<IncomeDto>> getIncomeList(
        @Parameter(description = "회원 식별 번호", example = "25") @RequestParam("memberId") Long memberId, 
        @Parameter(hidden = true) @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
            return new ResponseEntity<>(incomeService.incomeList(memberId, pageable), HttpStatus.OK);
    }


    // 수입 상세 조회
    @Operation(summary = "수입 상세 조회", description = "등록된 수입 정보들 중 특정 수입을 조회합니다.")
    @GetMapping("/{income-id}")
    public ResponseEntity<IncomeDto> getIncomeDetail(
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId) {
            return new ResponseEntity<>(incomeService.incomeDetail(incomeId), HttpStatus.OK);
    }


    // 수입 수정
    @Operation(summary = "수입 수정", description = "등록된 수입 정보들 중 특정 수입을 수정합니다.")
    @PatchMapping("/{income-id}")
    public ResponseEntity<IncomeDto> patchIncome(
        @Parameter(description = "수입 수정 내용") @RequestBody IncomeEditDto edit,
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId
        ) {
            return new ResponseEntity<>(incomeService.modifyIncome(edit, incomeId), HttpStatus.OK);
    }


    // 수입 삭제
    @Operation(summary = "수입 삭제", description = "등록된 수입 정보들 중 특정 수입을 삭제합니다.")
    @DeleteMapping("/{income-id}")
    public ResponseEntity<IncomeDeleteDto> deleteIncome(
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId
        ) {
            return new ResponseEntity<>(incomeService.deleteIncome(incomeId), HttpStatus.OK);
    }
    

}
