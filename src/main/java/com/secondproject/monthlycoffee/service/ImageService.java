package com.secondproject.monthlycoffee.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;
import com.secondproject.monthlycoffee.repository.ExpenseImageInfoRepository;

@Service
public class ImageService {
    @Autowired ExpenseImageInfoRepository imageRepo;
    public Map<String, Object> addImage(ExpenseImageInfo data) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        imageRepo.save(data);
        return map;
    }
}
