package com.secondproject.monthlycoffee.service;

import com.secondproject.monthlycoffee.dto.expense.*;
import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.ExpenseImageInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.Category;
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
import java.time.LocalDate;
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

    public MessageExpenseDto update(Long expenseNo, ExpenseDetailDto data) {
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

    public MessageExpenseDto putExpense(MultipartFile[] file, ExpenseDetailDto data, @RequestParam Long userNo) {
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
        if (startDate == null) {
            startDate = 0;
        }
        if (endDate == null) {
            endDate = 9999;
        }
        List<ExpenseInfo> entity = eRepo.searchTotalExpense(startDate, endDate, memberId).stream().toList();
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        String nickname = member.getNickname();
        Integer totalPrice = 0;
        for (ExpenseInfo expenseInfo : entity) {
            totalPrice += expenseInfo.getPrice();
        }
        return new TotalExpenseDto(nickname, totalPrice, startDate, endDate);
    }

    public MessageExpenseDto dummyData(Long userNo, Integer size) {
        String[] category = {"아메리카노", "카페라떼", "바닐라라떼", "카라멜 마끼아또", "카푸치노"};
        String[] brand = {"스타벅스", "투썸플레이스", "빽다방", "이디야커피", "할리스커피"};
        Boolean[] tumbler = {true, false};
        Taste[] taste = {Taste.valueOf("SWEET"), Taste.valueOf("SOUR"), Taste.valueOf("SAVORY"), Taste.valueOf("BITTER")};
        Mood[] mood = {Mood.valueOf("WORK"), Mood.valueOf("TALK"), Mood.valueOf("SELFIE")};
        CoffeeBean[] bean = {CoffeeBean.valueOf("BRAZIL"), CoffeeBean.valueOf("GUATEMALA"), CoffeeBean.valueOf("COLOMBIA"), CoffeeBean.valueOf("MEXICO"), CoffeeBean.valueOf("INDONESIA")};
        LikeHate[] hate = {LikeHate.valueOf("LIKE"), LikeHate.valueOf("HATE"), LikeHate.valueOf("SOSO")};
        for(int i=0; i<size; i++) {
            int random1 = (int) ((Math.random())*10%2);
            int random2 = (int) ((Math.random())*10%3);
            int random3 = (int) ((Math.random())*10%4);
            int random4 = (int) ((Math.random())*10%5);
            int randomM = (int) ((Math.random())*100%12)+1;
            int randomY = 0;
            int price = (int) (Math.round((Math.random()*10000)/100)*100);
            if(price < 1000) {
                price += 1000;
            }
            try {
                Thread.sleep(0);
                if (randomM == 2) {
                    randomY = (int) ((Math.random()) * 100 % 28)+1;
                } else if (randomM == 4 || randomM == 6 || randomM == 9 || randomM == 11) {
                    randomY = (int) ((Math.random()) * 100 % 30)+1;
                } else {
                    randomY = (int) ((Math.random()) * 100 % 31)+1;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
                LocalDate date = LocalDate.of(2023, randomM, randomY);

            ExpenseInfo entity1 = new ExpenseInfo(
                    category[random4],
                    brand[random4],
                    price,
                    "메모",
                    tumbler[random1],
                    taste[random3],
                    mood[random2],
                    bean[random4],
                    hate[random2],
                    random1,
                    date,
                    memberRepo.findById(userNo).orElseThrow());
            eRepo.save(entity1);
        }
        return new MessageExpenseDto(1L, +size+"개의 더미데이터가 등록되었습니다");
    }

    public List<ExpenseDetailDto> getCategory(Integer date, String keyword, Long memberId) {
        return eRepo.searchCategory(date, keyword, memberId).stream().map(ExpenseDetailDto::new).toList();
    }

    public List<ExpenseDetailDto> getBrand(Integer date, String keyword, Long memberId) {
        return eRepo.searchBrand(date, keyword, memberId).stream().map(ExpenseDetailDto::new).toList();
    }

}
