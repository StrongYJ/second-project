package com.secondproject.monthlycoffee.service;

import com.secondproject.monthlycoffee.dto.expense.DeleteExpenseDto;
import com.secondproject.monthlycoffee.dto.expense.ExpenseDetailDto;
import com.secondproject.monthlycoffee.dto.expense.ExpenseDto;
import com.secondproject.monthlycoffee.dto.post.PostDetailDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseInfoRepository eRepo;
    private final ImageService imageService;
    private final MemberInfoRepository memberRepo;
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

    public DeleteExpenseDto delete(Long id) {
        ExpenseInfo expense = eRepo.findById(id).orElseThrow();
        eRepo.delete(expense);
        return new DeleteExpenseDto(expense.getId(), "삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<ExpenseDetailDto> getExpense(Long memberId) {
        return eRepo.findByMember(memberRepo.findById(memberId).orElseThrow()).stream().map(ExpenseDetailDto::new).toList();
    }

    public ExpenseDetailDto update(Long expenseNo, ExpenseDto data) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
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

        return new ExpenseDetailDto(entity);
    }
}
