package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    
}
