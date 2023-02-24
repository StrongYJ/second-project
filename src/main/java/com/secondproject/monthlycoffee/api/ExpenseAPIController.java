package com.secondproject.monthlycoffee.api;

import java.util.*;

import com.secondproject.monthlycoffee.config.security.AuthMember;
import com.secondproject.monthlycoffee.config.security.dto.AuthDto;
import com.secondproject.monthlycoffee.dto.expense.*;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "지출 API", description = "지출 CRUD API")
@RequiredArgsConstructor
public class ExpenseAPIController {
    private final ExpenseService eService;

//    내용 및 이미지 동시에 입력 구현
//    프론트에서 구현 불가능으로 주석처리
//    @Operation(summary = "지출 등록", description = "지출 내용 및 이미지를 등록합니다.")
//    @PostMapping("")
//    public ResponseEntity<MessageExpenseDto> putExpense(@Parameter(description = "등록할 이미지") @RequestPart(required = false) MultipartFile[] file, @Parameter(description = "등록할 내용") ExpenseDetailDto data, @Parameter(description = "회원 번호", example = "1") @RequestParam Long userNo) {
//        return new ResponseEntity<>(eService.putExpense(file, data, userNo), HttpStatus.OK);
//    }

    @Operation(summary = "지출 등록", description = "지출 내용을 등록합니다.")
    @PostMapping("")
    public ResponseEntity<MessageExpenseDto> postExpense(
            @Parameter(description = "등록할 내용") @RequestBody ExpenseCreateDto data,
            @AuthMember AuthDto authDto) {
        return new ResponseEntity<>(eService.postExpense(data, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "이미지 등록", description = "이미지를 등록합니다.")
    @PostMapping(value = "/{expense-id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageExpenseDto> postExpenseImage(
            @Parameter(description = "등록할 이미지") @RequestPart MultipartFile[] file,
            @Parameter(description = "게시글 번호", example = "1") @PathVariable("expense-id") Long expenseId,
            @AuthMember AuthDto authDto) {
        return new ResponseEntity<>(eService.postExpenseImage(file, expenseId, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "회원 지출 조회", description = "회원 식별 번호만 입력시 그 회원의 전체 지출 내역이 조회됩니다. 평가(assessment)값 조회연월값(date)을 같이 넣으면 두조건 모두에 해당하는 지출 내역만 조회합니다. 각각 검색도 가능합니다.")
    @GetMapping("")
    public ResponseEntity<List<ExpenseDetailDto>> getExpenseList(
            @AuthMember AuthDto authDto,
            @Parameter(description = "지출 평가 목록", example = "LIKE") @RequestParam(required = false) LikeHate assessment,
            @Parameter(description = "년도(23)와 월(02)", example = "2302") Integer date) {
        return new ResponseEntity<>(eService.getExpense(authDto.id(), assessment, date), HttpStatus.OK);
    }

    @Operation(summary = "회원 총 지출액 조회", description = "회원의 총 지출 액 및 기간별 지출 액을 조회합니다. 시작과 끝 연월을 입력하시면 그 기간의 총액 산출, 시작연월 미입력시 끝 연월 포함 이전 기간 총 지출액, 끝 연월 미입력시 시작 연월 포함 이후 기간 총 지출액, 미입력시 총 지출액 조회입니다.")
    @GetMapping("/total")
    public ResponseEntity<TotalExpenseDto> getTotalExpense(
            @AuthMember AuthDto authDto,
            @Parameter(description = "년도(23)와 월(02)", example = "startDate=2302") @RequestParam(required = false, defaultValue = "") Integer startDate,
            @Parameter(description = "년도(22)와 월(03)", example = "&endDate=2303") Integer endDate) {
        return new ResponseEntity<>(eService.getTotalExpense(authDto.id(), startDate, endDate), HttpStatus.OK);
    }

    @Operation(summary = "지출 수정", description = "지출 식별 번호를 통해 등록된 지출을 수정합니다.")
    @PatchMapping("/{expense-id}")
    public ResponseEntity<MessageExpenseDto> updateExpense(
            @Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long expenseNo,
            @Parameter(description = "수정을 원하는 데이터") @RequestBody ExpenseCreateDto data,
            @AuthMember AuthDto authDto) {
        return new ResponseEntity<>(eService.updateExpense(expenseNo, data, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "지출 삭제", description = "지출 식별 번호를 통해 등록된 지출을 삭제합니다. 삭제시 연결되어있는 이미지가 같이 삭제됩니다.")
    @DeleteMapping("/{expense-id}")
    public ResponseEntity<MessageExpenseDto> deleteExpense(
            @Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long id,
            @AuthMember AuthDto authDto) {
        return new ResponseEntity<>(eService.deleteExpense(id, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "이미지 다운로드", description = "이미지를 다운로드할 수 있는 주소입니다.")
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage (
            @Parameter(description = "이미지 이름") @PathVariable String filename,
            HttpServletRequest request) throws Exception {
        return eService.getImage(filename, request);
    }

    @Operation(summary = "이미지 삭제", description = "이미지 식별 번호를 통해 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/image/{filename}")
    public ResponseEntity<MessageExpenseDto> deleteImage(
            @Parameter(description = "이미지 식별 번호", example = "1") @PathVariable("filename") String filename) {
        return new ResponseEntity<>(eService.deleteImage(filename), HttpStatus.OK);
    }

    @Operation(summary = "더미 제작", description = "원하는 회원의 더미를 size 개 생성합니다.")
    @PostMapping("/dummy")
    public ResponseEntity<MessageExpenseDto> postDummy(
            @RequestParam Long userNo,
            Integer size) {
        return new ResponseEntity<>(eService.dummyData(userNo, size), HttpStatus.OK);
    }

    @Operation(summary = "카테고리 조회", description = "회원의 카테고리를 검색합니다.")
    @GetMapping("/category")
    public ResponseEntity<List<ExpenseDetailDto>> getCategory(
            @AuthMember AuthDto authDto,
            @Parameter(description = "조회연월", example = "2302") @RequestParam Integer date,
            @Parameter(description = "카테고리", example = "아메리카노") String keyword) {
        return new ResponseEntity<>(eService.getCategory(date, keyword, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "브랜드 조회", description = "회원의 브랜드를 검색합니다.")
    @GetMapping("/brand")
    public ResponseEntity<List<ExpenseDetailDto>> getBrand(
            @AuthMember AuthDto authDto,
            @Parameter(description = "조회연월", example = "2302") @RequestParam Integer date,
            @Parameter(description = "브랜드", example = "스타벅스") String keyword) {
        return new ResponseEntity<>(eService.getBrand(date, keyword, authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "사용자의 취향 조회", description = "좋아요를 누른 내용을 토대로 회원의 취향을 추정합니다.")
    @GetMapping("/preference")
    public ResponseEntity<MessageExpenseDto> getBrand(@AuthMember AuthDto authDto) {
        return new ResponseEntity<>(eService.likeStyle(authDto.id()), HttpStatus.OK);
    }

    @Operation(summary = "텀블러 사용 횟수 랭킹", description = "텀블러 사용 횟수를 토대로 랭킹을 냅니다. 분기마다 초기화됩니다.")
    @GetMapping("/rank")
    public ResponseEntity<List<TumblerRank>> rankTumbler() {
        return new ResponseEntity<>(eService.rankTumbler(), HttpStatus.OK);
    }
}
