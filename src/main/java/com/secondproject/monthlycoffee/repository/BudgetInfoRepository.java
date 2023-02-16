package com.secondproject.monthlycoffee.repository;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface BudgetInfoRepository extends JpaRepository<BudgetInfo, Long> {
    Page<BudgetInfo> findByMember(MemberInfo member, Pageable pageable);
    Boolean existsByYearMonth(String yearMonth);

    @Query(value = "SELECT e From BudgetInfo e WHERE e.yearMonth Like yearMonth", nativeQuery = true)
    public List<BudgetDto> statsBudgetByYearMonth(@Param("yearMonth") YearMonth yearMonth);
    
}
