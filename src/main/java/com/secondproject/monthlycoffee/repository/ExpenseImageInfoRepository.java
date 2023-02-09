package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;

public interface ExpenseImageInfoRepository extends JpaRepository<ExpenseImageInfo, Long> {
}
