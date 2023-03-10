package com.secondproject.monthlycoffee.doyouee;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.secondproject.monthlycoffee.dto.budget.BudgetEditDto;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import jakarta.validation.constraints.AssertTrue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import com.secondproject.monthlycoffee.service.BudgetService;
import com.secondproject.monthlycoffee.service.IncomeService;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Transactional
@Slf4j
public class MyTest {
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private BudgetInfoRepository budgetRepo;
    @Autowired private IncomeInfoRepository incomeRepo;
    @Autowired private IncomeService incomeService;
    @Autowired private BudgetService budgetService;
    @Autowired private EntityManager em;
    
    private MemberInfo member;
    private BudgetInfo budget;

    private List<IncomeInfo> incomeList = new ArrayList<>();
    
    Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void init() {
        member = new MemberInfo(AuthDomain.KAKAO, "abcd12345efghi", "?????????", LocalDate.of(1990, 05, 05), Gender.FEMALE);
        budget = new BudgetInfo(500000, member);
        // log.info(member.toString());

        // 202301 -> sum 50005
        incomeList.add(new IncomeInfo(10005, "??????", LocalDate.of(2023, 1, 1), member));
        incomeList.add(new IncomeInfo(25000, "????????????", LocalDate.of(2023, 1, 5), member));
        incomeList.add(new IncomeInfo(15000, "??????", LocalDate.of(2023, 1, 31), member));
        // 202201 -> sum 10000
        incomeList.add(new IncomeInfo(10000, "??????", LocalDate.of(2022, 1, 15), member));
        // 202302 -> sum 6500
        incomeList.add(new IncomeInfo(1000, "????????????", LocalDate.of(2023, 2, 9), member));
        incomeList.add(new IncomeInfo(5500, "????????????", LocalDate.of(2023, 02, 22), member));
        
        memberRepo.save(member);
        incomeRepo.saveAll(incomeList);
        budgetRepo.save(budget);
    }
    
    @DisplayName("?????? ????????????")
    @Test
    void bidgetAllListView() {
        Assertions.assertThat(budgetRepo.findByMember(member, pageable).getContent().size()).isEqualTo(1);
    }

    @DisplayName("?????? ????????????")
    @Test
    void budgetDetailView() {
        Assertions.assertThat(budgetRepo.findById(budget.getId()).get().getAmount()).isEqualTo(500000);
    }

    @DisplayName("?????? ??????")
    @Test
    void budgetModify() {
        assertThrows(IllegalArgumentException.class, () -> {
            budgetService.modifyBudget(new BudgetEditDto(730000), 1L, 1L);
        }, "????????? ?????? ?????? ???????????? ????????? ??? ????????????.");
    }

    @DisplayName("?????? ????????????")
    @Test
    void memberAllListView() {
        Assertions.assertThat(memberRepo.findAll(pageable));
    }
    
    @DisplayName("?????? ????????????")
    @Test
    void memberDetailView() {
        Assertions.assertThat(memberRepo.findById(member.getId()).get());
    }


    @DisplayName("?????? ????????????")
    @Test
    void incomeListView() { Assertions.assertThat(incomeRepo.findByMember(member, pageable).getContent().size()).isEqualTo(6); }

    @DisplayName("?????? ????????????")
    @Test
    void incomeDetailView() { Assertions.assertThat(incomeRepo.findById(incomeList.get(0).getId()).get().getAmount()).isEqualTo(10005); }

    @DisplayName("?????? ??????")
    @Test
    void incomeDelete() {
        incomeService.deleteIncome(member.getId(), incomeList.get(0).getId());
        Assertions.assertThat(incomeRepo.findByMember(member, pageable).getContent().size()).isEqualTo(5);
    }

    @DisplayName("?????? ????????? ??????")
    @Test
    void sumIncomeByYearMonth(){
        IncomeSumDto list = incomeRepo.sumByYearMonth(member, LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 31));
        log.info(member.toString());
        System.out.println(list.getYearMonth());
        System.out.println(list.getSum());
    }

    @DisplayName("??????+?????? ????????? ?????????")
    @Test
    void listIncomeByYearMonth(){
        List<IncomeInfo> list = incomeRepo.findByYearMonth(member, 2301);
        log.info(member.toString());
        System.out.println(list.size());
        for(IncomeInfo a : list){
            System.out.println(a.getAmount());
            System.out.println(a.getDate());
        }
    }


}
