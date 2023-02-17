package com.secondproject.monthlycoffee.service;

import com.secondproject.monthlycoffee.dto.expense.*;
import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.repository.ExpenseImageInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseInfoRepository eRepo;
    private final ImageService imageService;
    private final MemberInfoRepository memberRepo;
    private final ExpenseImageInfoRepository imageRepo;
    @Value("${file.dir}") String path;

    public ResponseEntity<Resource> getImage (String filename, HttpServletRequest request) throws Exception {
        Path folderLocation = Paths.get(path);
        Path targetFile = folderLocation.resolve(filename);
        Resource r = null;
        try {
            r = new UrlResource(targetFile.toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=\"" + URLEncoder.encode(filename, "UTF-8") + "\"")
                .body(r);
    }

    public MessageExpenseDto delete(Long id) {
        ExpenseInfo expense = eRepo.findById(id).orElseThrow();
        eRepo.delete(expense);
        return new MessageExpenseDto(expense.getId(), "삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<ExpenseDetailDto> getExpense(Long memberId, LikeHate assessment, Integer date) {
        if(date == null && assessment == null) {
            return eRepo.findByMember(memberRepo.findById(memberId).orElseThrow()).stream().map(ExpenseDetailDto::new).toList();
        }
        if(date == null) {
            return eRepo.searchLikeHate(assessment, memberId).stream().map(ExpenseDetailDto::new).toList();
        }
        if(assessment == null) {
            return eRepo.searchDate(date, memberId).stream().map(ExpenseDetailDto::new).toList();
        }
        return eRepo.searchtotalList(date, assessment, memberId).stream().map(ExpenseDetailDto::new).toList();
    }

    public MessageExpenseDto update(Long expenseNo, ExpenseDto data) {
        ExpenseInfo entity = eRepo.findById(expenseNo).orElseThrow();

        if(data.getCategory() != null) entity.setCategory(data.getCategory());
        if(data.getBrand() != null) entity.setBrand(data.getBrand());
        if(data.getPrice() != null) entity.setPrice(data.getPrice());
        if(data.getMemo() != null) entity.setMemo(data.getMemo());
        if(data.getTumbler() != null) entity.setTumbler(data.getTumbler());
        if(data.getTaste() != null) entity.setTaste(data.getTaste());
        if(data.getMood() != null) entity.setMood(data.getMood());
        if(data.getBean() != null) entity.setBean(data.getBean());
        if(data.getLikeHate() != null) entity.setLikeHate(data.getLikeHate());
        if(data.getPayment() != null) entity.setPayment(data.getPayment());
        if(data.getDate() != null) entity.setDate(data.getDate());
        eRepo.save(entity);

        return new MessageExpenseDto(entity.getId(), "수정되었습니다.");
    }

    public MessageExpenseDto putExpense(MultipartFile[] file, ExpenseDto data, @RequestParam Long userNo) {
        Path folderLocation = Paths.get(path);
        ExpenseInfo entity1 = new ExpenseInfo(data.getCategory(), data.getBrand(), data.getPrice(), data.getMemo(), data.getTumbler(), data.getTaste(), data.getMood(), data.getBean(), data.getLikeHate(), data.getPayment(), data.getDate(), memberRepo.findById(userNo).orElseThrow());
        eRepo.save(entity1);
        if(file != null) {
            for (int a = 0; a < file.length; a++) {
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
        }
        return new MessageExpenseDto(entity1.getId(), "등록되었습니다.");
    }

    public MessageExpenseDto deleteImage(Long id) {
        ExpenseImageInfo image = imageRepo.findById(id).orElseThrow();
        imageRepo.delete(image);
        return new MessageExpenseDto(image.getId(), "삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public TotalExpenseDto getTotalExpense(Long memberId, Integer startDate, Integer endDate) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        if(startDate == null) {
            startDate = 0;
        }
        if(endDate == null) {
            endDate = 9999;
        }
        List<ExpenseDetailDto> entity = eRepo.searchTotalExpense(startDate, endDate, memberId).stream().map(ExpenseDetailDto::new).toList();
        Integer totalPrice = 0;
        for (ExpenseDetailDto expenseDetailDto : entity) {
            totalPrice += expenseDetailDto.getPrice();
        }
        return new TotalExpenseDto(member.getNickname(), totalPrice, startDate, endDate);
    }
}
