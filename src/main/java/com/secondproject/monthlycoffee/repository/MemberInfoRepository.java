package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.MemberInfo;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    Optional<MemberInfo> findByUid(String uid);
    Boolean existsByNickname(String yearMonth);
}
