package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.LovePostInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface LovePostInfoRepository extends JpaRepository<LovePostInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from LovePostInfo l where l.post = :post")
    void deleteAllByPostInBatch(@Param("post") PostInfo post);

    Optional<LovePostInfo> findByPostAndMember(PostInfo post, MemberInfo member);

    long countByPost(PostInfo post);
}
