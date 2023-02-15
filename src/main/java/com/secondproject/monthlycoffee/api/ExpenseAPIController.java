package com.secondproject.monthlycoffee.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.secondproject.monthlycoffee.dto.expense.*;
import com.secondproject.monthlycoffee.dto.post.PostDetailDto;

import com.secondproject.monthlycoffee.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "지출 관리", description = "지출 CRUD API")
@RequiredArgsConstructor
public class ExpenseAPIController {
    private final ExpenseService eService;

    @Operation(summary = "지출 등록 API", description = "지출 내용 및 이미지를 등록합니다.")
    @PostMapping("")
    public ResponseEntity<CreateExpenseDto> putExpense(@Parameter(description = "등록할 이미지") @RequestPart @Nullable MultipartFile[] file, @Parameter(description = "등록할 내용") ExpenseDto data, @Parameter(description = "회원 번호", example = "1") @RequestParam Long userNo) {
        return new ResponseEntity<>(eService.putExpense(file, data, userNo), HttpStatus.OK);
    }

    @Operation(summary = "이미지 다운로드 API", description = "이미지를 다운로드할 수 있는 URI 주소입니다.")
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage (@Parameter(description = "이미지 이름") @PathVariable String filename, HttpServletRequest request) throws Exception {
        return eService.getImage(filename, request);
    }

    @Operation(summary = "지출 삭제 API", description = "지출 식별 번호를 통해 등록된 지출을 삭제합니다. 삭제시 연결되어있는 이미지가 같이 삭제됩니다.")
    @DeleteMapping("/{expense-id}")
    public ResponseEntity<DeleteExpenseDto> deleteExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long id) {
        return new ResponseEntity<>(eService.delete(id), HttpStatus.OK);
    }

    @Operation(summary = "회원 지출 조회 API", description = "회원 식별 번호만 입력시 그 회원의 전체 지출 내역이 조회됩니다. 조회연월값(date)을 넣으면 해당하는 지출 내역만 조회됩니다.")
    @GetMapping("/{member-id}")
    public ResponseEntity<List<ExpenseDetailDto>> getExpenseList(@Parameter(description = "회원 식별 번호") @PathVariable("member-id") Long memberId, @Parameter(description = "연도(22)와 월(02)", example = "2202") @RequestParam(required = false, defaultValue = "") Integer date) {
        return new ResponseEntity<>(eService.getExpense(memberId, date), HttpStatus.OK);
    }

    @Operation(summary = "지출 수정 API", description = "지출 식별 번호를 통해 등록된 지출을 수정합니다.")
    @PutMapping("/{expense-id}")
    public ResponseEntity<UpdateExpenseDto> updateExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long expenseNo, @RequestBody ExpenseDto data) {
        return new ResponseEntity<>(eService.update(expenseNo, data), HttpStatus.OK);
    }

    @Operation(summary = "이미지 삭제 API", description = "이미지 식별 번호를 통해 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/image/{image-id}")
    public ResponseEntity<DeleteExpenseDto> deleteImage(@Parameter(description = "이미지 식별 번호", example = "1") @PathVariable("image-id") Long id) {
        return new ResponseEntity<>(eService.deleteImage(id), HttpStatus.OK);
    }
}
