package com.secondproject.monthlycoffee.doyouee;

import java.time.LocalDate;

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

    MemberInfo member = new MemberInfo("abcd12345efghi", "하이룽", LocalDate.of(1990, 05, 05), Gender.FEMALE);
    BudgetInfo budget = new BudgetInfo(500000, member);
    Pageable pageable = PageRequest.of(0, 10);

    @Test
    void getBudgetList() {
        memberRepo.save(member);
        budgetRepo.save(budget);
        System.out.println(budgetRepo.findByMember(memberRepo.findById(1l).get(), pageable));
    }
}
