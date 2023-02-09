package com.secondproject.monthlycoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.secondproject.monthlycoffee.entity.CommentInfo;

public interface CommentInfoRepository extends JpaRepository<CommentInfo, Long> {
    
}
