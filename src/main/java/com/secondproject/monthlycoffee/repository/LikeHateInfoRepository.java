package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.LikeHateInfo;

public interface LikeHateInfoRepository extends JpaRepository<LikeHateInfo, Long> {
    
}
