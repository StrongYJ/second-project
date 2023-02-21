package com.secondproject.monthlycoffee.strongyj;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.entity.BudgetInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.repository.BudgetInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

@SpringBootTest
@Transactional
public class JpaAuditiongTest {
    
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private BudgetInfoRepository budgetRepo;
    
    @Test
    @Rollback(false)
    void auditingTest() {
        MemberInfo member = new MemberInfo(AuthDomain.KAKAO, "fwrekfnwk", "test", LocalDate.of(199, 1, 1), Gender.MALE);
        BudgetInfo budgetInfo = new BudgetInfo(10000, member);

        memberRepo.save(member);
        budgetRepo.save(budgetInfo);
    }
}
