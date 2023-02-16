package com.secondproject.monthlycoffee.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.dto.income.ImcomeSumDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface IncomeInfoRepository extends JpaRepository<IncomeInfo, Long> {
    Page<IncomeInfo> findByMember(MemberInfo member, Pageable pageable);

    // @Query(value = "SELECT i FROM IncomeInfo ORDER BY date")
    // List<IncomeInfo> findAllByDate(@Param("date") LocalDate date);
    
    @Query("select SUBSTRING(cast(i.date as text), 3,5) as yearMonth, sum(i.amount) as sum from IncomeInfo i where i.member = :member and i.date between :start and :end group by yearMonth order by yearMonth desc")
    List<ImcomeSumDto> sumByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
}
