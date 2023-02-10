package com.secondproject.monthlycoffee.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.repository.BudgetInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetInfoRepository budgetRepo;
    private final MemberInfoRepository memberRepo;

    // 예산 전체 리스트 조회
    public Page<BudgetDto> budgetList(Long id, Pageable pageable) {
        // Page<GenreEntity> page = genreRepository.findByGenreNameContains(keyword, pageable);
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        return budgetRepo.findByMember(member, pageable).map(BudgetDto::new);
    }

    // 예산 상세 조회
    public BudgetDto budgetDetail(Long id) {
        // return budgetRepo.findByMember(member, pageable).map(BudgetDto::new);
        BudgetInfo budget = budgetRepo.findById(id).orElseThrow();
        return new BudgetDto(budget);
    }
}
