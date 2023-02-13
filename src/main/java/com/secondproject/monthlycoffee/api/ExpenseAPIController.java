package com.secondproject.monthlycoffee.api;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.monthlycoffee.dto.expense.ExpenseDto;
import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/expense")
public class ExpenseAPIController {
    @Autowired ExpenseInfoRepository eRepo;
    @Autowired ImageService imageService;
    @Autowired MemberInfoRepository memberRepo;
    @Value("${file.dir}") String path;

    @PutMapping("/add")
    public ResponseEntity<Object> putAlarmImage(@RequestPart MultipartFile file[], ExpenseDto data, @RequestParam Long userNo) {
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
            Path targerFile = folderLocation.resolve(saveFilename);
            try {
                Files.copy(file[a].getInputStream(), targerFile, StandardCopyOption.REPLACE_EXISTING);
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

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage (@PathVariable String filename, HttpServletRequest request) throws Exception {
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
}
