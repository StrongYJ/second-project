package com.secondproject.monthlycoffee.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.secondproject.monthlycoffee.dto.expense.ExpenseDto;
import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.repository.ExpenseImageInfoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {
    @Autowired ExpenseImageInfoRepository imageRepo;
    @Autowired ExpenseInfoRepository eRepo;
    public Map<String, Object> addImage(ExpenseImageInfo data) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        imageRepo.save(data);
        return map;
    }
}
