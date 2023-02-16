package com.secondproject.monthlycoffee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface PostInfoRepository extends JpaRepository<PostInfo, Long> {
    boolean existsByExpense(ExpenseInfo expense);

    @Override
    @EntityGraph(attributePaths = {"expense"})
    Optional<PostInfo> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"expense"})
    List<PostInfo> findAll();

    @Override
    @EntityGraph(attributePaths = {"expense"})
    Page<PostInfo> findAll(Pageable pageable);
}
