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
import jakarta.validation.constraints.Max;
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

//    내용 및 이미지 동시에 입력 구현
//    프론트에서 구현 불가능으로 주석처리
//    public MessageExpenseDto putExpense(MultipartFile[] file, ExpenseDetailDto data, Long userNo) {
//        Path folderLocation = Paths.get(path);
//        ExpenseInfo entity1 = new ExpenseInfo(data.getCategory(), data.getBrand(), data.getPrice(), data.getMemo(), data.getTumbler(), data.getTaste(), data.getMood(), data.getBean(), data.getLikeHate(), data.getPayment(), data.getDate(), memberRepo.findById(userNo).orElseThrow());
//        eRepo.save(entity1);
//        if(file != null) {
//            for (int a = 0; a < file.length; a++) {
//                String originFileName = file[a].getOriginalFilename();
//                String[] split = originFileName.split("\\.");
//                String ext = split[split.length - 1];
//                String filename = "";
//                for (int i = 0; i < split.length - 1; i++) {
//                    filename += split[i];
//                }
//                String saveFilename = "coffee" + "_";
//                Calendar c = Calendar.getInstance();
//                saveFilename += c.getTimeInMillis() + "." + ext;
//                Path targetFile = folderLocation.resolve(saveFilename);
//                try {
//                    Files.copy(file[a].getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                ExpenseImageInfo entity2 = new ExpenseImageInfo(null, saveFilename, filename, entity1);
//                imageService.addImage(entity2);
//            }
//        }
//        return new MessageExpenseDto(entity1.getId(), "등록되었습니다.");
//    }

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
        if(nickname == null) {
            nickname = "회원";
        }
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
        CoffeeBean[] bean = {CoffeeBean.valueOf("BRAZIL"), CoffeeBean.valueOf("GUATEMALA"), CoffeeBean.valueOf("COLOMBIA"), CoffeeBean.valueOf("MEXICO"), CoffeeBean.valueOf("INDONESIA"), CoffeeBean.VIETNAM};
        LikeHate[] hate = {LikeHate.valueOf("LIKE"), LikeHate.valueOf("HATE"), LikeHate.valueOf("SOSO")};
        for(int i=0; i<size; i++) {
            int randomTumbler = (int) ((Math.random())*10000%2);
            int randomPayment = (int) ((Math.random())*10000%2);
            int randomMood = (int) ((Math.random())*10000%3);
            int randomHate = (int) ((Math.random())*10000%3);
            int randomTaste = (int) ((Math.random())*10000%4);
            int randomCategory = (int) ((Math.random())*10000%5);
            int randomBrand = (int) ((Math.random())*10000%5);
            int randomBean = (int) ((Math.random())*10000%6);
            int randomM = (int) ((Math.random())*10000%12)+1;
            int randomY = 0;
            int price = (int) (Math.round((Math.random()*10000)/100)*100);
            if(price < 1000) {
                price += 1000;
            }
            try {
                Thread.sleep(0);
                if (randomM == 2) {
                    randomY = (int) ((Math.random())*10000%28)+1;
                } else if (randomM == 4 || randomM == 6 || randomM == 9 || randomM == 11) {
                    randomY = (int) ((Math.random())*10000%30)+1;
                } else {
                    randomY = (int) ((Math.random())*10000%31)+1;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
                LocalDate date = LocalDate.of(2023, randomM, randomY);

            ExpenseInfo entity1 = new ExpenseInfo(
                    category[randomCategory],
                    brand[randomBrand],
                    price,
                    "메모",
                    tumbler[randomTumbler],
                    taste[randomTaste],
                    mood[randomMood],
                    bean[randomBean],
                    hate[randomHate],
                    randomPayment,
                    date,
                    memberRepo.findById(userNo).orElseThrow());
            eRepo.save(entity1);
        }
        return new MessageExpenseDto(userNo, +size+"개의 더미데이터가 등록되었습니다");
    }

    public List<ExpenseDetailDto> getCategory(Integer date, String keyword, Long memberId) {
        return eRepo.searchCategory(date, keyword, memberId).stream().map(ExpenseDetailDto::new).toList();
    }

    public List<ExpenseDetailDto> getBrand(Integer date, String keyword, Long memberId) {
        return eRepo.searchBrand(date, keyword, memberId).stream().map(ExpenseDetailDto::new).toList();
    }

    public MessageExpenseDto likeStyle(Long memberId) {
        List<ExpenseInfo> entity = eRepo.findByMember(memberRepo.findById(memberId).orElseThrow()).stream().toList();
        String nickname = memberRepo.findById(memberId).orElseThrow().getNickname();

        int likeCount = 0;

        int countWork = 0; int countTalk = 0; int countSelfie = 0; String likeMood = "";

        int countBrazil = 0; int countGuatemala = 0; int countColombia = 0;
        int countMexico = 0; int countIndonesia = 0; int countVietnam = 0; String likeBean = "";

        int countSweet = 0; int countSavory = 0; int countBitter = 0; int countSour = 0; String likeTaste = "";

        for (int i=0; i<entity.size(); i++) {
            if (entity.get(i).getLikeHate().equals(LikeHate.valueOf("LIKE"))) {
                likeCount ++;
                {
                    if (entity.get(i).getMood().equals(Mood.WORK)) countWork++;
                    else if (entity.get(i).getMood().equals(Mood.TALK)) countTalk++;
                    else if (entity.get(i).getMood().equals(Mood.SELFIE)) countSelfie++;
                }
                {
                    if (entity.get(i).getBean().equals(CoffeeBean.BRAZIL)) countBrazil++;
                    else if (entity.get(i).getBean().equals(CoffeeBean.GUATEMALA)) countGuatemala++;
                    else if (entity.get(i).getBean().equals(CoffeeBean.COLOMBIA)) countColombia++;
                    else if (entity.get(i).getBean().equals(CoffeeBean.MEXICO)) countMexico++;
                    else if (entity.get(i).getBean().equals(CoffeeBean.INDONESIA)) countIndonesia++;
                    else if (entity.get(i).getBean().equals(CoffeeBean.VIETNAM)) countVietnam++;
                }
                {
                    if (entity.get(i).getTaste().equals(Taste.SWEET)) countSweet++;
                    else if (entity.get(i).getTaste().equals(Taste.SAVORY)) countSavory++;
                    else if (entity.get(i).getTaste().equals(Taste.BITTER)) countBitter++;
                    else if (entity.get(i).getTaste().equals(Taste.SOUR)) countSour++;
                }
            }
        }

        if(likeCount < 10) {
            return new MessageExpenseDto(memberId, nickname+"님의 정보가 부족합니다. 조금만 더 이용해 주세요~");
        }

        int rankMood = Math.max(Math.max(countWork, countTalk), countSelfie);
        if(rankMood == countWork) likeMood = "공부하기 좋은";
        else if(rankMood == countTalk) likeMood = "수다떨기 좋은";
        else likeMood = "사진찍기 좋은";

        int rankBean = Math.max(Math.max(Math.max(Math.max(Math.max(countBrazil, countGuatemala), countColombia), countMexico), countIndonesia), countVietnam);
        if(rankBean == countBrazil) likeBean = "브라질산";
        else if(rankBean == countGuatemala) likeBean = "과테말라산";
        else if(rankBean == countColombia) likeBean = "콜롬비아산";
        else if(rankBean == countMexico) likeBean = "멕시코산";
        else if(rankBean == countIndonesia) likeBean = "인도네시아산";
        else likeBean = "베트남산";

        int rankTaste = Math.max(Math.max(Math.max(countSweet, countSavory), countBitter), countSour);
        if(rankTaste == countSweet) likeTaste = "단맛이 나는";
        else if(rankTaste == countSavory) likeTaste = "고소한맛이 나는";
        else if(rankTaste == countBitter) likeTaste = "쓴맛이 나는";
        else likeTaste = "신맛이 나는";

        return new MessageExpenseDto(memberId, nickname+"님은 "+likeMood+" 분위기의 카페에서 "+likeBean+"의 "+likeTaste+" 커피를 좋아하세요.");
    }

    public MessageExpenseDto putExpense(ExpenseCreateDto data, Long memberId) {
        ExpenseInfo entity1 = new ExpenseInfo(data.getCategory(), data.getBrand(), data.getPrice(), data.getMemo(), data.getTumbler(), data.getTaste(), data.getMood(), data.getBean(), data.getLikeHate(), data.getPayment(), data.getDate(), memberRepo.findById(memberId).orElseThrow());
        if(data.getCategory() == null || data.getBrand() == null || data.getPrice() == null || data.getDate() == null) {
            return new MessageExpenseDto(entity1.getId(), "필수 정보 누락입니다.");
        }
        eRepo.save(entity1);
        return new MessageExpenseDto(entity1.getId(), "등록되었습니다.");
    }

    public MessageExpenseDto putExpenseImage(MultipartFile[] file, Long expenseId) {
        Path folderLocation = Paths.get(path);
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
                ExpenseImageInfo entity2 = new ExpenseImageInfo(null, saveFilename, filename, eRepo.findById(expenseId).orElseThrow());
                imageService.addImage(entity2);
            }
        }
        return new MessageExpenseDto(expenseId, "등록되었습니다.");
    }
}
