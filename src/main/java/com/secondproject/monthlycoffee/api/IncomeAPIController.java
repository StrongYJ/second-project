package com.secondproject.monthlycoffee.api;

import java.time.YearMonth;
import java.util.List;

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

import com.secondproject.monthlycoffee.config.security.AuthMember;
import com.secondproject.monthlycoffee.config.security.dto.AuthDto;
import com.secondproject.monthlycoffee.dto.income.IncomeAvgDto;
import com.secondproject.monthlycoffee.dto.income.IncomeDto;
import com.secondproject.monthlycoffee.dto.income.IncomeEditDto;
import com.secondproject.monthlycoffee.dto.income.IncomeExpenseListDto;
import com.secondproject.monthlycoffee.dto.income.IncomeMessageDto;
import com.secondproject.monthlycoffee.dto.income.IncomeNewDto;
import com.secondproject.monthlycoffee.dto.income.IncomeRankDto;
import com.secondproject.monthlycoffee.dto.income.IncomeSumDto;
import com.secondproject.monthlycoffee.service.IncomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
@Tag(name = "수입 API", description = "수입 정보 CRUD API")
public class IncomeAPIController {
    private final IncomeService incomeService;


    // 수입 등록
    @Operation(summary = "수입 등록", description = "수입을 등록합니다.")
    @PostMapping("")
    public ResponseEntity<IncomeDto> postIncome(
        @Parameter(description = "등록 할 수입 정보") @RequestBody IncomeNewDto data,
        @AuthMember AuthDto authDto
        ) {
            return new ResponseEntity<>(incomeService.newIncome(data, authDto.id()), HttpStatus.CREATED);
    }


    // 수입 전체 조회
    @Operation(summary = "수입 전체 리스트 조회", description = "등록된 수입 정보들을 10개 단위로 보여줍니다.")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<IncomeDto>> getIncomeList(
        @AuthMember AuthDto authDto,
        @Parameter(hidden = true) @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
            return new ResponseEntity<>(incomeService.incomeList(authDto.id(), pageable), HttpStatus.OK);
    }


    // 수입 상세 조회
    @Operation(summary = "수입 상세 조회", description = "등록된 수입 정보들 중 특정 수입을 조회합니다.")
    @GetMapping("/{income-id}")
    public ResponseEntity<IncomeDto> getIncomeDetail(
        @AuthMember AuthDto authDto,
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId) {
            return new ResponseEntity<>(incomeService.incomeDetail(authDto.id(), incomeId), HttpStatus.OK);
    }


    // 수입 수정
    @Operation(summary = "수입 수정", description = "등록된 수입 정보들 중 특정 수입을 수정합니다.")
    @PatchMapping("/{income-id}")
    public ResponseEntity<IncomeDto> patchIncome(
        @AuthMember AuthDto authDto,
        @Parameter(description = "수입 수정 내용") @RequestBody IncomeEditDto edit,
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId
        ) {
            return new ResponseEntity<>(incomeService.modifyIncome(authDto.id(), edit, incomeId), HttpStatus.OK);
    }


    // 수입 삭제
    @Operation(summary = "수입 삭제", description = "등록된 수입 정보들 중 특정 수입을 삭제합니다.")
    @DeleteMapping("/{income-id}")
    public ResponseEntity<IncomeMessageDto> deleteIncome(
        @AuthMember AuthDto authDto,
        @Parameter(description = "수입 식별 번호", example = "1") @PathVariable("income-id") Long incomeId
        ) {
            return new ResponseEntity<>(incomeService.deleteIncome(authDto.id(), incomeId), HttpStatus.OK);
    }
    

    // 수입 연월별 합계
    @Operation(summary = "수입 연월별 합계 조회", description = "등록된 수입 정보들 중 연월별 합계를 조회합니다.")
    @GetMapping("/stats/sum")
    public ResponseEntity<IncomeSumDto> sumIncomeByYearMonth(
        @Parameter(description = "조회하려는 연도와 달", example = "2023-03") @RequestParam YearMonth date,
        @AuthMember AuthDto authDto
    ){
        return new ResponseEntity<IncomeSumDto>(incomeService.sumIncomeByYearMonth(date, authDto.id()), HttpStatus.OK);
    }


    // 수입 + 지출 연월별 리스트
    @Operation(summary = "수입+지출 연월별 리스트 조회", description = "등록된 수입과 지출 정보들 중 연월별 리스트를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<IncomeExpenseListDto>> listIncomeByYearMonth(
        @Parameter(description = "조회하려는 연도와 달", example = "2023") @RequestParam Integer date,
        @AuthMember AuthDto authDto
    ) {
        return new ResponseEntity<List<IncomeExpenseListDto>>(incomeService.searchIncomeByYearMonth(date, authDto.id()), HttpStatus.OK);
    }


    // 수입 연월별 평균
    @Operation(summary = "수입 연월별 평균 조회", description = "등록된 수입 정보들 중 연월별 평균을 조회합니다.")
    @GetMapping("/stats/avg")
    public ResponseEntity<IncomeAvgDto> avgIncomeByYearMonth(
        @Parameter(description = "조회하려는 연도와 달", example = "2023-03") @RequestParam YearMonth date,
        @AuthMember AuthDto authDto
    ){
        return new ResponseEntity<IncomeAvgDto>(incomeService.avgIncomeByYearMonth(date, authDto.id()), HttpStatus.OK);
    }



    // 수입 연도별 랭킹
    @Operation(summary = "수입 연도별 조회시 월별 랭킹 조회", description = "등록된 수입 정보들을 연도별로 조회시 월별 랭킹을 조회합니다.")
    @GetMapping("/rank")
    public ResponseEntity<List<IncomeRankDto>> rankingByYear(
        @Parameter(description = "조회하려는 연도", example = "2023") @RequestParam String year,
        @AuthMember AuthDto authDto
    ) {
        return new ResponseEntity<List<IncomeRankDto>>(incomeService.rankIncomeByYear(year, authDto.id()), HttpStatus.OK);
    }


    // 수입 키워드 검색
    @Operation(summary = "수입 키워드 검색 조회", description = "등록된 수입 정보들을 키워드로 검색 후 목록을 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<IncomeDto>> searchByKeyword(
        @Parameter(description = "검색하려는 키워드", example = "용돈") @RequestParam String keyword,
        @AuthMember AuthDto authDto
    ) {
        return new ResponseEntity<>(incomeService.searchIncomeByKeyword(keyword, authDto.id()), HttpStatus.OK);
    }


}
