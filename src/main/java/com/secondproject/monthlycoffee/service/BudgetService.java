package com.secondproject.monthlycoffee.service;

import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.budget.BudgetDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetEditDto;
import com.secondproject.monthlycoffee.dto.budget.BudgetNewDto;
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
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        if(member.getId()!=id) {
            throw new IllegalArgumentException("본인만 수정이 가능합니다."); 
        }
        return budgetRepo.findByMember(member, pageable).map(BudgetDto::new);
    }
    
    
    // 예산 상세 조회
    @Transactional(readOnly = true)
    public BudgetDto budgetDetail(Long id) {
        BudgetInfo budget = budgetRepo.findById(id).orElseThrow();
        return new BudgetDto(budget);
    }
    

    // 예산 등록
    public BudgetDto newBudget(BudgetNewDto data, Long id) {
        if(budgetRepo.existsByYearMonth(YearMonth.now().toString())) {
            throw new IllegalArgumentException("해당 월에 이미 존재하는 예산 정보가 있습니다.");
        }
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        if(member.getId()!=id) {
            throw new IllegalArgumentException("본인만 수정이 가능합니다."); 
        }
        if(data.amount() <= 0){
            throw new NoSuchElementException("예산은 0원 이상의 금액이어야 합니다."); 
        }
        BudgetInfo newBudget = new BudgetInfo(data.amount(), member);
        budgetRepo.save(newBudget);
        return new BudgetDto(newBudget);
    }
    

    // 예산 수정
    public BudgetDto modifyBudget(BudgetEditDto edit, Long id) {
        BudgetInfo budget = budgetRepo.findById(id).orElseThrow();
        MemberInfo member = memberRepo.findById(budget.getMember().getId()).orElseThrow();
        if(member.getId()!=id) {
            throw new IllegalArgumentException("본인만 수정이 가능합니다."); 
        }
        if(!(budget.getYearMonth().equals(YearMonth.now().toString()))){
            throw new IllegalArgumentException("현재와 다른 달의 예산액은 수정할 수 없습니다."); 
        }
        if(edit.amount() <= 0){
            throw new NoSuchElementException("예산은 0원 이상의 금액이어야 합니다."); 
        }
        budget.modifyAmount(edit.amount());
        return new BudgetDto(budget);
    }


    
}
