package com.secondproject.monthlycoffee.strongyj.service;

import com.secondproject.monthlycoffee.entity.*;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.*;
import com.secondproject.monthlycoffee.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private BudgetInfoRepository budgetInfoRepository;
    @Autowired private IncomeInfoRepository incomeInfoRepository;
    @Autowired private ExpenseInfoRepository expenseInfoRepository;
    @Autowired private LovePostInfoRepository lovePostInfoRepository;
    @Autowired private CommentInfoRepository commentInfoRepository;
    @Autowired private PostInfoRepository postInfoRepository;
    @PersistenceContext
    private EntityManager em;
    private List<MemberInfo> dummyMembers = new ArrayList<>();
    private List<ExpenseInfo> dummyExpense = new ArrayList<>();
    private List<PostInfo> dummyPosts = new ArrayList<>();

    private long memberId;
    @BeforeEach
    void init() {
        MemberInfo memberInfo = new MemberInfo(AuthDomain.KAKAO, UUID.randomUUID().toString(), null, null, null);
        memberRepo.save(memberInfo);
        memberId = memberInfo.getId();
        for(int i = 0; i < 10; i++) {
            MemberInfo memberInfo1 = new MemberInfo(AuthDomain.KAKAO, UUID.randomUUID().toString(), null, null, null);
            memberRepo.save(memberInfo1);
            ExpenseInfo expenseInfo = new ExpenseInfo("category", "brand",
                    ThreadLocalRandom.current().nextInt(20000), null, ThreadLocalRandom.current().nextBoolean(),
                    Taste.BITTER, Mood.SELFIE, CoffeeBean.BRAZIL, LikeHate.SOSO, 0, LocalDate.now(), memberInfo1);
            expenseInfoRepository.save(expenseInfo);
            PostInfo postInfo = new PostInfo(UUID.randomUUID().toString(), expenseInfo);
            postInfoRepository.save(postInfo);
            dummyMembers.add(memberInfo1);
            dummyExpense.add(expenseInfo);
            dummyPosts.add(postInfo);
        }
        memberRepo.saveAll(dummyMembers);
    }

    @Test
    void 멤버삭제() {
        // given
        MemberInfo m = memberRepo.findById(memberId).get();
        for(int i = 0; i < 10; i++) {
            budgetInfoRepository.save(new BudgetInfo(ThreadLocalRandom.current().nextInt(15000),m));
            incomeInfoRepository.save(new IncomeInfo(ThreadLocalRandom.current().nextInt(1000000), null, LocalDate.now(), m));
            expenseInfoRepository.save(new ExpenseInfo("category", "brand",
                    ThreadLocalRandom.current().nextInt(20000), null, ThreadLocalRandom.current().nextBoolean(),
                    Taste.BITTER, Mood.SELFIE, CoffeeBean.BRAZIL, LikeHate.SOSO, 0, LocalDate.now(), m));
            lovePostInfoRepository.save(new LovePostInfo(m, postInfoRepository.save(new PostInfo("test", dummyExpense.get(i)))));
            commentInfoRepository.save(new CommentInfo(UUID.randomUUID().toString(), m, dummyPosts.get(i)));
        }

        // when
        memberService.deleteMember(memberId);

        // then
        assertThat(memberRepo.existsById(memberId)).isFalse();

    }
}
