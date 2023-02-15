package com.secondproject.monthlycoffee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface IncomeInfoRepository extends JpaRepository<IncomeInfo, Long> {
    Page<IncomeInfo> findByMember(MemberInfo member, Pageable pageable);
    
}
