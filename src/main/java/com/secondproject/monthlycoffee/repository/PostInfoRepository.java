package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {
    boolean existByExpense(ExpenseInfo expense);
    
}
