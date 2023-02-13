package com.secondproject.monthlycoffee.doyouee;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.repository.BudgetInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

@SpringBootTest
@Transactional
public class MyTest {
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private BudgetInfoRepository budgetRepo;

    private MemberInfo member;
    private BudgetInfo budget;
    
    Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void init() {
        member = new MemberInfo("abcd12345efghi", "하이룽", LocalDate.of(1990, 05, 05), Gender.FEMALE);
        budget = new BudgetInfo(500000, member);
        memberRepo.save(member);
        budgetRepo.save(budget);
    }
    
    @Test
    void getBudgetList() { // 예산 전체 조회
        Assertions.assertThat(budgetRepo.findByMember(member, pageable).getContent().size()).isEqualTo(1);
    }

    @Test
    void getBudgetDetail() { // 예산 상세 조회
        Assertions.assertThat(budgetRepo.findById(budget.getId()).get().getAmount()).isEqualTo(500000);
    }

    @Test
    void modifyBudget() { // 예산 수정
        
    }
    
}
