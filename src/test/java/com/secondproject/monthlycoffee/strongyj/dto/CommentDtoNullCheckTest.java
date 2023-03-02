package com.secondproject.monthlycoffee.strongyj.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.entity.type.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.secondproject.monthlycoffee.dto.comment.CommentDto;
import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.repository.CommentInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CommentDtoNullCheckTest {
    
    @Autowired private CommentInfoRepository commentRepo;
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private PostInfoRepository postRepo;
    @Autowired private ExpenseInfoRepository expenseRepo;
    @Autowired private EntityManager em;

    private MemberInfo member;
    private PostInfo post;
    private ExpenseInfo expense;

    @BeforeEach
    void init() {
        member = new MemberInfo(AuthDomain.KAKAO, "sfksdnafka", "test", LocalDate.of(1999, 1, 1), Gender.MALE);
        expense = new ExpenseInfo("Latte", "StarBucks", 7000, null, false, Taste.SWEET, Mood.TALK, CoffeeBean.COLOMBIA, LikeHate.LIKE, 1, LocalDate.now(), member);
        post = new PostInfo("맛있다", expense);
        
        memberRepo.save(member);
        expenseRepo.save(expense);
        postRepo.save(post);
        
    }

    @DisplayName("멤버가 존재할 때")
    @Test
    void existMember() {
        CommentInfo comment = new CommentInfo("test comment", member, post);
        Assertions.assertDoesNotThrow(() -> {
            CommentDto commentDto = new CommentDto(comment);
            assertEquals(member.getNickname(), commentDto.nickname());
        });
    }

    @DisplayName("멤버가 존재하지 않을 때")
    @Test
    void noMember() {
        expense.deleteMember();
        memberRepo.deleteById(member.getId());
        em.flush();
        em.clear();
        
        CommentInfo comment = new CommentInfo("test comment", memberRepo.findById(member.getId()).orElse(null), post);
        Assertions.assertDoesNotThrow(() -> {
            CommentDto commentDto = new CommentDto(comment);
            Assertions.assertEquals(null, commentDto.nickname());
        });
    }


}
