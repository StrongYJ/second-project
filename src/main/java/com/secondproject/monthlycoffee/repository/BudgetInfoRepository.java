package com.secondproject.monthlycoffee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface BudgetInfoRepository extends JpaRepository<BudgetInfo, Long> {

    Page<BudgetInfo> findByMember(MemberInfo member, Pageable pageable);
    Boolean existsByYearMonth(String yearMonth);
    
}
