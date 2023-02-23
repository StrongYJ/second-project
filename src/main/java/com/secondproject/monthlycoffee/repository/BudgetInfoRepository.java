package com.secondproject.monthlycoffee.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetRankDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetSumDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface BudgetInfoRepository extends JpaRepository<BudgetInfo, Long> {
    Page<BudgetInfo> findByMember(MemberInfo member, Pageable pageable);
    Boolean existsByYearMonth(String yearMonth);

    @Query(value = "SELECT e From BudgetInfo e WHERE e.yearMonth Like yearMonth", nativeQuery = true)
    public List<BudgetDto> statsBudgetByYearMonth(@Param("yearMonth") YearMonth yearMonth);
    
    @Query("select e from BudgetInfo e where e.member = :member and SUBSTRING(cast(e.yearMonth as text), 1,4) = :year order by e.yearMonth")
    List<BudgetInfo> findByYear(@Param("member") MemberInfo member, @Param("year") String year);
    
    @Query("select SUBSTRING(cast(e.yearMonth as text), 1,4) as year, sum(e.amount) as sum from BudgetInfo e where e.member = :member and SUBSTRING(cast(e.yearMonth as text), 1,4) = :year order by e.yearMonth")
    BudgetSumDto sumByYear(@Param("member") MemberInfo member, @Param("year") String year);

    @Query("select sum(e.amount)as sum, SUBSTRING(cast(e.yearMonth as text), 1,7) as yearMonth, rank() over(order by e.amount desc) as rank from BudgetInfo e where e.member = :member and SUBSTRING(cast(e.yearMonth as text), 1,4) = :year group by yearMonth")
    List<BudgetRankDto> rankByYear(@Param("member") MemberInfo member, @Param("year") String year);

    @Modifying(clearAutomatically = true)
    @Query("delete from BudgetInfo b where b.member = :member")
    void deleteByMember(@Param("member") MemberInfo member);
}
