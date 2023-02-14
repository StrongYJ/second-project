package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.LikeHateInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface LikeHateInfoRepository extends JpaRepository<LikeHateInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from LikeHateInfo l where l.post = :post")
    void deleteAllByPostInBatch(@Param("post") PostInfo post);

}
