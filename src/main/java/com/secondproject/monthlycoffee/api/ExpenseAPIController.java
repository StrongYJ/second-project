package com.secondproject.monthlycoffee.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.secondproject.monthlycoffee.dto.expense.DeleteExpenseDto;
import com.secondproject.monthlycoffee.dto.expense.ExpenseDetailDto;
import com.secondproject.monthlycoffee.dto.post.PostDetailDto;
import com.secondproject.monthlycoffee.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.monthlycoffee.dto.expense.ExpenseDto;
import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.service.ImageService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "지출 관리", description = "지출 CRUD API")
public class ExpenseAPIController {
    @Autowired ExpenseInfoRepository eRepo;
    @Autowired ImageService imageService;
    @Autowired MemberInfoRepository memberRepo;
    @Autowired ExpenseService eService;
    @Value("${file.dir}") String path;

    @Operation(summary = "지출 등록 API", description = "지출 내용 및 이미지를 등록합니다.")
    @PostMapping("")
    public ResponseEntity<Object> putExpense(@Parameter(description = "등록할 이미지") @RequestPart @Nullable MultipartFile file[], @Parameter(description = "등록할 내용") ExpenseDto data, @Parameter(description = "회원 번호", example = "1") @RequestParam Long userNo) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Path folderLocation = Paths.get(path);
        ExpenseInfo entity1 = new ExpenseInfo(data.getCategory(), data.getBrand(), data.getPrice(), data.getMemo(), data.getTumbler(), data.getTaste(), data.getMood(), data.getBean(), data.getLikeHate(), data.getPayment(), data.getDate(), memberRepo.findById(userNo).orElseThrow());
        eRepo.save(entity1);
        for (int a=0; a<file.length; a++) {
            String originFileName = file[a].getOriginalFilename();
            String[] split = originFileName.split("\\.");
            String ext = split[split.length - 1];
            String filename = "";
            for (int i = 0; i < split.length - 1; i++) {
                filename += split[i];
            }
            String saveFilename = "coffee" + "_";
            Calendar c = Calendar.getInstance();
            saveFilename += c.getTimeInMillis() + "." + ext;
            Path targetFile = folderLocation.resolve(saveFilename);
            try {
                Files.copy(file[a].getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ExpenseImageInfo entity2 = new ExpenseImageInfo(null, saveFilename, filename, entity1);
            imageService.addImage(entity2);
        }
        map.put("status", true);
        map.put("message", "등록되었습니다.");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Operation(summary = "이미지 다운로드 API", description = "이미지를 다운로드할 수 있는 URI 주소입니다.")
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage (@Parameter(description = "이미지 이름") @PathVariable String filename, HttpServletRequest request) throws Exception {
        return eService.getImage(filename, request);
    }

    @Operation(summary = "지출 삭제 API", description = "지출 식별 번호를 통해 등록된 지출을 삭제합니다. 삭제시 연결되어있는 이미지가 같이 삭제됩니다.")
    @DeleteMapping("/{expense-id}")
    public ResponseEntity<DeleteExpenseDto> deleteExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long id) {
        DeleteExpenseDto deleteExpense = eService.delete(id);
        return new ResponseEntity<>(deleteExpense, HttpStatus.OK);
    }

    @Operation(summary = "지출 조회 API", description = "한 회원의 전체 지출 내역을 조회합니다.")
    @GetMapping("/{member-id}")
    public ResponseEntity<List<ExpenseDetailDto>> getExpenseList(@Parameter(description = "회원 식별 번호") @PathVariable("member-id") Long memberId) {
        return new ResponseEntity<>(eService.getExpense(memberId), HttpStatus.OK);
    }

    @Operation(summary = "지출 수정 API", description = "지출 식별 번호를 통해 등록된 지출을 수정합니다.")
    @PutMapping("/{expense-id}")
    public ResponseEntity<ExpenseDetailDto> updateExpense(@Parameter(description = "지출 식별 번호", example = "1") @PathVariable("expense-id") Long expenseNo, @RequestBody ExpenseDto data) {
        return new ResponseEntity<>(eService.update(expenseNo, data), HttpStatus.OK);
    }
}
