package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    Optional<MemberInfo> findByAuthDomainAndUid(AuthDomain authDomain, String uid);
    Boolean existsByNickname(String yearMonth);
}
