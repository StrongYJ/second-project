package com.secondproject.monthlycoffee.strongyj.repository;

import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.CommentInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class CommentInfoRepositoryTest {
    @Autowired private CommentInfoRepository commentInfoRepository;
    @Autowired private MemberInfoRepository memberInfoRepository;
    @Autowired private ExpenseInfoRepository expenseInfoRepository;
    @Autowired private PostInfoRepository postInfoRepository;

    @Test
    void delete() {
        // given
        MemberInfo testMember = new MemberInfo(AuthDomain.KAKAO, "test", null, null, null);
        ExpenseInfo testExpense = new ExpenseInfo("test", "tesbrand", 1000, null,
                false, Taste.BITTER, Mood.SELFIE, CoffeeBean.BRAZIL, LikeHate.SOSO, 1, LocalDate.now(), testMember);
        PostInfo testPost = new PostInfo("test", testExpense);
        CommentInfo comment = new CommentInfo("test", testMember, testPost);
        memberInfoRepository.save(testMember);
        expenseInfoRepository.save(testExpense);
        postInfoRepository.save(testPost);
        commentInfoRepository.save(comment);

        // when
        Optional<CommentInfo> opCommentByIdAndMemberId = commentInfoRepository.findByIdAndMemberId(comment.getId(), testMember.getId());

        // then
        Assertions.assertTrue(opCommentByIdAndMemberId.isPresent());
        CommentInfo commentByIdAndMemberId = opCommentByIdAndMemberId.get();
        Assertions.assertDoesNotThrow(() -> {
            commentInfoRepository.delete(commentByIdAndMemberId);
        });
        Assertions.assertTrue(commentInfoRepository.findById(commentByIdAndMemberId.getId()).isEmpty());

    }
}
