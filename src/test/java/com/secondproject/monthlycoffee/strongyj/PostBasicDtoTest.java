package com.secondproject.monthlycoffee.strongyj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PostBasicDtoTest {
    @Autowired
    private MemberInfoRepository memberRepo;
    @Autowired
    private PostInfoRepository postRepo;
    @Autowired
    private ExpenseInfoRepository expenseRepo;

    private MemberInfo member;
    private PostInfo post;
    private ExpenseInfo expense;

    @BeforeEach
    void init() {
        member = new MemberInfo("sfksdnafka", "test", LocalDate.of(1999, 1, 1), Gender.MALE);
        expense = new ExpenseInfo("Latte", "StarBucks", 7000, null, false, Taste.SWEET, Mood.TALK, CoffeeBean.COLOMBIA,
                LikeHate.LIKE, 1, LocalDate.now(), member);
        post = new PostInfo("맛있다", expense);

        memberRepo.save(member);
        expenseRepo.save(expense);
        postRepo.save(post);

    }

    @Test
    void getPostBasic() {
        Assertions.assertDoesNotThrow(() -> {
            PostBasicDto postBasicDto = new PostBasicDto(post);
            assertEquals(0, postBasicDto.likeNumber());
        });
    }
}
