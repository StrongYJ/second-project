package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;

public interface ExpenseInfoRepository extends JpaRepository<ExpenseInfo, Long> {
    @Query("select e from ExpenseInfo e join e.member m where e.id = :id and m.id = :memberId")
    Optional<ExpenseInfo> findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}
