package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.BudgetInfo;

public interface BudgetInfoRepository extends JpaRepository<BudgetInfo, Long> {
    
}
