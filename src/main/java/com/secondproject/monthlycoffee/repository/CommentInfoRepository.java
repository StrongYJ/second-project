package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface CommentInfoRepository extends JpaRepository<CommentInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from CommentInfo c where c.post = :post")
    void deleteAllByPostInBatch(@Param("post") PostInfo post);
}
