package com.secondproject.monthlycoffee.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.dto.income.IncomeSumDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface IncomeInfoRepository extends JpaRepository<IncomeInfo, Long> {
    Page<IncomeInfo> findByMember(MemberInfo member, Pageable pageable);

    @Query("select SUBSTRING(cast(i.date as text), 1,7) as yearMonth, sum(i.amount) as sum from IncomeInfo i where i.member = :member and i.date between :start and :end group by yearMonth order by yearMonth desc")
    IncomeSumDto sumByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("select i from IncomeInfo i where i.member = :member and i.date between :start and :end order by i.date")
    List<IncomeInfo> findByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);

}

/*
    연월별 평균
    TypedQuery<Double> query = em.createQuery("SELECT AVG(o.orderAmount) FROM Order o", Double.class);
    List<Double> resultList = query.getResultList();
 */