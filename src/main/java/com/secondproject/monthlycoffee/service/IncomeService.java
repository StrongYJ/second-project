package com.secondproject.monthlycoffee.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.expense.ExpenseDetailDto;
import com.secondproject.monthlycoffee.dto.income.IncomeAvgDto;
import com.secondproject.monthlycoffee.dto.income.IncomeDto;
import com.secondproject.monthlycoffee.dto.income.IncomeEditDto;
import com.secondproject.monthlycoffee.dto.income.IncomeExpenseListDto;
import com.secondproject.monthlycoffee.dto.income.IncomeListDetailDto;
import com.secondproject.monthlycoffee.dto.income.IncomeMessageDto;
import com.secondproject.monthlycoffee.dto.income.IncomeNewDto;
import com.secondproject.monthlycoffee.dto.income.IncomeRankDto;
import com.secondproject.monthlycoffee.dto.income.IncomeSumDto;
import com.secondproject.monthlycoffee.dto.post.ExpenseImageDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.IncomeInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeService {
    private final IncomeInfoRepository incomeRepo;
    private final MemberInfoRepository memberRepo;
    private final ExpenseInfoRepository expenseRepo;

    // 수입 등록
    public IncomeDto newIncome(IncomeNewDto data, Long memberId) {
        if(memberRepo.findById(memberId).isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        }
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        IncomeInfo newIncome = new IncomeInfo(data.amount(), data.note(), data.date(), member);
        if(data.amount() <= 0){
            throw new NoSuchElementException("수입은 0원 이상의 금액이어야 합니다."); 
        }
        incomeRepo.save(newIncome);
        return new IncomeDto(newIncome);
    }


    // 수입 전체 조회
    @Transactional(readOnly = true)
    public Page<IncomeDto> incomeList(Long memberId, Pageable pageable) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        return incomeRepo.findByMember(member, pageable).map(IncomeDto::new);
    }


    // 수입 상세 조회
    @Transactional(readOnly = true)
    public IncomeDto incomeDetail(Long memberId, Long incomeId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        IncomeInfo income = incomeRepo.findById(incomeId).orElseThrow();
        if(member.getId() != income.getMember().getId()) {
            throw new IllegalArgumentException("본인이 아니면 조회가 불가능합니다."); 
        }
        return new IncomeDto(income);
    }


    // 수입 수정
    public IncomeDto modifyIncome(Long memberId, IncomeEditDto edit, Long incomeId) {
        IncomeInfo income = incomeRepo.findById(incomeId).orElseThrow();
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        if(member.getId() != income.getMember().getId()) {
            throw new IllegalArgumentException("본인이 아니면 수정이 불가능합니다."); 
        }
        if(edit.amount() <= 0){
            throw new NoSuchElementException("수입은 0원 이상의 금액이어야 합니다."); 
        }
        income.modifyBudgetDetail(edit.amount(), edit.note(), edit.date());
        return new IncomeDto(income);
    }


    // 수입 삭제
    public IncomeMessageDto deleteIncome(Long memberId, Long incomeId) {
        IncomeInfo income = incomeRepo.findById(incomeId).orElseThrow();
        MemberInfo member = memberRepo.findById(income.getMember().getId()).orElseThrow();
        if(member.getId() != income.getMember().getId()) {
            throw new IllegalArgumentException("본인이 아니면 삭제가 불가능합니다."); 
        }
        incomeRepo.delete(income);
        return new IncomeMessageDto(incomeId, "수입이 삭제되었습니다.");
    }


    // 수입 연월별 합계
    public IncomeSumDto sumIncomeByYearMonth(YearMonth date, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        LocalDate firstDate = date.atDay(1); 
        LocalDate endDate = date.atEndOfMonth();  
        IncomeSumDto income = incomeRepo.sumByYearMonth(member, firstDate, endDate);
        return income;
    }


    // 수입+지출 연월별 리스트
    public List<IncomeExpenseListDto> searchIncomeByYearMonth(YearMonth date, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        LocalDate firstDate = date.atDay(1); 
        LocalDate endDate = date.atEndOfMonth(); 
        
        List<IncomeInfo> incomeInfos = incomeRepo.findByYearMonth(member, firstDate, endDate);
        List<ExpenseInfo> expenseInfos = expenseRepo.findByYearMonth(member, firstDate, endDate);
        
        List<IncomeExpenseListDto> incomeExpenseList = new ArrayList<IncomeExpenseListDto>();
        List<IncomeListDetailDto> incomeList = new ArrayList<IncomeListDetailDto>();
        List<ExpenseDetailDto> expenseList = new ArrayList<ExpenseDetailDto>();

        IncomeExpenseListDto incomeExpense = new IncomeExpenseListDto();

        for(IncomeInfo i : incomeInfos) {
            incomeExpense.setYearMonth(date);
            IncomeListDetailDto incomeListSet = new IncomeListDetailDto();

            incomeListSet.setId(i.getId());
            incomeListSet.setAmount(i.getAmount());
            incomeListSet.setNote(i.getNote());
            incomeListSet.setDate(i.getDate());

            incomeList.add(incomeListSet);
            incomeExpense.setIncome(incomeList);
        }
            
        for(ExpenseInfo e : expenseInfos) {
            ExpenseDetailDto expenseListSet = new ExpenseDetailDto();
            expenseListSet.setId(e.getId());
            expenseListSet.setCategory(e.getCategory());
            expenseListSet.setBrand(e.getBrand());
            expenseListSet.setPrice(e.getPrice());
            expenseListSet.setMemo(e.getMemo());
            expenseListSet.setTumbler(e.getTumbler());
            expenseListSet.setTaste(e.getTaste());
            expenseListSet.setMood(e.getMood());
            expenseListSet.setBean(e.getBean());
            expenseListSet.setLikeHate(e.getLikeHate());
            expenseListSet.setPayment(e.getPayment());
            expenseListSet.setDate(e.getDate());
            expenseListSet.setImages(e.getExpenseImages().stream().map(ExpenseImageDto::new).toList());
            expenseList.add(expenseListSet);
        }
        incomeExpense.setExpense(expenseList);
        incomeExpenseList.add(incomeExpense);

        return incomeExpenseList;
    }


    // 수입 연월별 평균
    public IncomeAvgDto avgIncomeByYearMonth(YearMonth date, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        LocalDate firstDate = date.atDay(1); 
        LocalDate endDate = date.atEndOfMonth();  
        IncomeAvgDto income = incomeRepo.avgByYearMonth(member, firstDate, endDate);
        return income;
    }


    // 수입 연도별 랭킹
    public List<IncomeRankDto> rankIncomeByYear(String year, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        List<IncomeRankDto> income = incomeRepo.rankByYear(member, year);
        return income;
    }


    // 수입 키워드 검색
    public List<IncomeDto> searchIncomeByKeyword(String keyword, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        List<IncomeDto> income = incomeRepo.findByNoteContainingAndMember(keyword, member).stream().map(IncomeDto::new).toList();
        System.out.println(income);
        return income;
    }



}
