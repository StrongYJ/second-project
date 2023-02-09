package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;

public interface ExpenseInfoRepository extends JpaRepository<ExpenseInfo, Long> {
    
}
