package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.IncomeInfo;

public interface IncomeInfoRepository extends JpaRepository<IncomeInfo, Long> {
    
}
