package com.secondproject.monthlycoffee.api;

import java.util.*;

import com.secondproject.monthlycoffee.dto.expense.*;

import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "지출 API", description = "지출 CRUD API")
@RequiredArgsConstructor
public class ExpenseAPIController {
    private final ExpenseService eService;

    @Operation(summary = "지출 등록 API", description = "지출 내용 및 이미지를 등록합니다.")
    @PostMapping("")
    public ResponseEntity<MessageExpenseDto> putExpense(@Parameter(description = "등록할 이미지") @RequestPart(required = false) MultipartFile[] file, @Parameter(description = "등록할 내용") ExpenseDto data, @Parameter(description = "회원 번호", example = "1") @RequestParam Long userNo) {
        return new ResponseEntity<>(eService.putExpense(file, data, userNo), HttpStatus.OK);
    }

    @Operation(summary = "회원 지출 조회 API", description = "회원 식별 번호만 입력시 그 회원의 전체 지출 내역이 조회됩니다. 평가(assessment)값 조회연월값(date)을 같이 넣으면 두조건 모두에 해당하는 지출 내역만 조회합니다. 각각 검색도 가능합니다.")
    @GetMapping("/{member-id}")
    public ResponseEntity<List<ExpenseDetailDto>> getExpenseList(@Parameter(description = "회원 식별 번호") @PathVariable("member-id") Long memberId, @Parameter(description = "지출 평가 목록", example = "LIKE") @RequestParam(required = false) LikeHate assessment, @Parameter(description = "년도(23)와 월(02)", example = "2302") Integer date) {
        return new ResponseEntity<>(eService.getExpense(memberId, assessment, date), HttpStatus.OK);
    }

    @Operation(summary = "회원 총 지출액 조회 API", description = "회원의 총 지출 액 및 기간별 지출 액을 조회합니다. 시작과 끝 연월을 입력하시면 그 기간의 총액 산출, 시작연월 미입력시 끝 연월 포함 이전 기간 총 지출액, 끝 연월 미입력시 시작 연월 포함 이후 기간 총 지출액, 미입력시 총 지출액 조회입니다.")
    @GetMapping("/total/{member-id}")
    public ResponseEntity<TotalExpenseDto> getTotalExpense(@Parameter(description = "회원 식별 번호") @PathVariable("member-id") Long memberId, @Parameter(description = "년도(23)와 월(02)", example = "startDate=2302") @RequestParam(required = false, defaultValue = "") Integer startDate, @Parameter(description = "년도(22)와 월(03)", example = "&endDate=2303") Integer endDate) {
        return new ResponseEntity<>(eService.getTotalExpense(memberId, startDate, endDate), HttpStatus.OK);
    }

    @Operation(summary = "지출 수정 API", description = "지출 식별 번호를 통해 등록된 지출을 수정합니다.")
    @PatchMapping("/{expense-id}")
    public ResponseEntity<MessageExpenseDto> updateExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long expenseNo, @RequestBody ExpenseDto data) {
        return new ResponseEntity<>(eService.update(expenseNo, data), HttpStatus.OK);
    }

    @Operation(summary = "지출 삭제 API", description = "지출 식별 번호를 통해 등록된 지출을 삭제합니다. 삭제시 연결되어있는 이미지가 같이 삭제됩니다.")
    @DeleteMapping("/{expense-id}")
    public ResponseEntity<MessageExpenseDto> deleteExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long id) {
        return new ResponseEntity<>(eService.delete(id), HttpStatus.OK);
    }

    @Operation(summary = "이미지 다운로드 API", description = "이미지를 다운로드할 수 있는 주소입니다.")
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage (@Parameter(description = "이미지 이름") @PathVariable String filename, HttpServletRequest request) throws Exception {
        return eService.getImage(filename, request);
    }

    @Operation(summary = "이미지 삭제 API", description = "이미지 식별 번호를 통해 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/image/{image-id}")
    public ResponseEntity<MessageExpenseDto> deleteImage(@Parameter(description = "이미지 식별 번호", example = "1") @PathVariable("image-id") Long id) {
        return new ResponseEntity<>(eService.deleteImage(id), HttpStatus.OK);
    }
}
