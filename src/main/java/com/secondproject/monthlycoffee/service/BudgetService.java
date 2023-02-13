package com.secondproject.monthlycoffee.service;

import java.time.YearMonth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetEditDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.repository.BudgetInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {
    private final BudgetInfoRepository budgetRepo;
    private final MemberInfoRepository memberRepo;


    // 예산 전체 리스트 조회
    @Transactional(readOnly = true)
    public Page<BudgetDto> budgetList(Long id, Pageable pageable) {
        // Page<GenreEntity> page = genreRepository.findByGenreNameContains(keyword, pageable);
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        return budgetRepo.findByMember(member, pageable).map(BudgetDto::new);
    }


    // 예산 상세 조회
    public BudgetDto budgetDetail(Long id) {
        BudgetInfo budget = budgetRepo.findById(id).orElseThrow();
        return new BudgetDto(budget);
    }
    

    // 예산 등록
    public BudgetDto newBudget(BudgetDto data, Long id) {
        if(budgetRepo.existsByYearMonth(YearMonth.now().toString())) {
            throw new IllegalArgumentException("해당 월에 이미 존재하는 예산 정보가 있습니다.");
        }
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        BudgetInfo newBudget = new BudgetInfo(data.amount(), member);
        budgetRepo.save(newBudget);
        return new BudgetDto(newBudget);
    }
    

    // 예산 수정
    public BudgetDto modifybudget(BudgetEditDto edit, Long id) {
        BudgetInfo budget = budgetRepo.findById(id).orElseThrow();
        if(!(budget.getYearMonth().equals(YearMonth.now().toString()))){
            throw new IllegalArgumentException("현재와 다른 달의 예산액은 수정할 수 없습니다."); 
        }
        budget.modifyAmount(edit.amount());
        return new BudgetDto(budget);
    }
}
