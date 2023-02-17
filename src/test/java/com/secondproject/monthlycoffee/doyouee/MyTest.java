package com.secondproject.monthlycoffee.doyouee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.income.IncomeDto;
import com.secondproject.monthlycoffee.dto.income.IncomeExpenseListDto;
import com.secondproject.monthlycoffee.dto.income.IncomeSumDto;
import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.repository.BudgetInfoRepository;
import com.secondproject.monthlycoffee.repository.IncomeInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Transactional
@Slf4j
public class MyTest {
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private BudgetInfoRepository budgetRepo;
    @Autowired private IncomeInfoRepository incomeRepo;
    @Autowired private EntityManager em;
    
    private MemberInfo member;
    private BudgetInfo budget;
    private List<IncomeInfo> incomeList = new ArrayList<>();
    
    Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void init() {
        member = new MemberInfo("abcd12345efghi", "하이룽", LocalDate.of(1990, 05, 05), Gender.FEMALE);
        budget = new BudgetInfo(500000, member);
        // log.info(member.toString());

        // 202301 -> sum 50000
        incomeList.add(new IncomeInfo(10000, null, LocalDate.of(2023, 1, 1), member));
        incomeList.add(new IncomeInfo(25000, null, LocalDate.of(2023, 1, 5), member));
        incomeList.add(new IncomeInfo(15000, null, LocalDate.of(2023, 1, 31), member));
        // 202201 -> sum 10000
        incomeList.add(new IncomeInfo(10000, null, LocalDate.of(2022, 1, 15), member));
        // 202302 -> sum 6000
        incomeList.add(new IncomeInfo(1000, null, LocalDate.of(2023, 2, 9), member));
        incomeList.add(new IncomeInfo(5000, null, LocalDate.of(2023, 02, 22), member));
        
        memberRepo.save(member);
        incomeRepo.saveAll(incomeList);
        budgetRepo.save(budget);
    }
    
    @Test
    void 예산전체조회() {
        Assertions.assertThat(budgetRepo.findByMember(member, pageable).getContent().size()).isEqualTo(1);
    }

    @Test
    void 예산상세조회() {
        Assertions.assertThat(budgetRepo.findById(budget.getId()).get().getAmount()).isEqualTo(500000);
    }

    @Test
    void 예산수정() {
        
    }
    
    @Test
    void 회원전체조회() {
        Assertions.assertThat(memberRepo.findAll(pageable));
    }
    
    @Test
    void 회원상세조회() {
        Assertions.assertThat(memberRepo.findById(member.getId()).get());
    }

    @Test
    void 수입연월별합계(){
        IncomeSumDto list = incomeRepo.sumByYearMonth(member, LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 31));
        log.info(member.toString());
        System.out.println(list.getYearMonth());
        System.out.println(list.getSum());
    }


    @Test
    void 수입연월별리스트(){
        List<IncomeInfo> list = incomeRepo.findByYearMonth(member, LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 31));
        log.info(member.toString());
        System.out.println(list.size());
        for(IncomeInfo a : list){
            System.out.println(a.getAmount());
            System.out.println(a.getDate());
        }
    }

// 1년 예산

}
