package com.secondproject.monthlycoffee.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.dto.income.IncomeAvgDto;
import com.secondproject.monthlycoffee.dto.income.IncomeRankDto;
import com.secondproject.monthlycoffee.dto.income.IncomeSumDto;
import com.secondproject.monthlycoffee.dto.income.IncomeListDetailDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface IncomeInfoRepository extends JpaRepository<IncomeInfo, Long> {
    Page<IncomeInfo> findByMember(MemberInfo member, Pageable pageable);

    @Query("select SUBSTRING(cast(i.date as text), 1,7) as yearMonth, sum(i.amount) as sum from IncomeInfo i where i.member = :member and i.date between :start and :end group by yearMonth order by yearMonth desc")
    IncomeSumDto sumByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("select SUBSTRING(cast(i.date as text), 1,7) as yearMonth, avg(i.amount) as avg from IncomeInfo i where i.member = :member and i.date between :start and :end group by yearMonth order by yearMonth desc")
    IncomeAvgDto avgByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("select i from IncomeInfo i where i.member = :member and i.date between :start and :end order by i.date")
    List<IncomeInfo> findByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("select sum(i.amount)as sum, SUBSTRING(cast(i.date as text), 1,7) as yearMonth, rank() over(order by i.amount desc) as rank from IncomeInfo i where i.member = :member and SUBSTRING(cast(i.date as text), 1,4) = :year group by yearMonth")
    List<IncomeRankDto> rankByYear(@Param("member") MemberInfo member, @Param("year") String year);

    List<IncomeInfo> findByNoteContainingAndMember(@Param("keyword") String keyword, @Param("member") MemberInfo member);

}
