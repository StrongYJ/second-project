package com.secondproject.monthlycoffee.strongyj.service;

import com.secondproject.monthlycoffee.dto.comment.CommentCreateDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDeleteDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDto;
import com.secondproject.monthlycoffee.dto.comment.CommentModifyDto;
import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.*;
import com.secondproject.monthlycoffee.service.CommentService;
import com.secondproject.monthlycoffee.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberInfoRepository memberRepo;
    @Autowired
    private ExpenseInfoRepository expenseRepo;
    @Autowired
    private PostInfoRepository postRepo;
    @Autowired
    private CommentInfoRepository commentRepo;

    private long postNumber = 0;

    // 더미 데이터 만들기
    @BeforeAll
    void init() {
        List<MemberInfo> members = new ArrayList<>();

        CoffeeBean[] coffeeBeans = CoffeeBean.values();
        Gender[] genders = Gender.values();
        LikeHate[] likeHates = LikeHate.values();
        Mood[] moods = Mood.values();
        Taste[] tastes = Taste.values();

        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();


        for(int i = 0; i < 20; i++) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            MemberInfo member = new MemberInfo(
                    AuthDomain.KAKAO,
                    UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                    LocalDate.ofEpochDay(randomDay), genders[random.nextInt(genders.length)]
            );
            memberRepo.save(member);
            members.add(member);
        }
        for(int i = 0; i < 100; i++) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for(int j = 0; j < random.nextInt(5); j++) {
                long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
                MemberInfo memberInfo = members.get(random.nextInt(members.size()));
                ExpenseInfo expense = new ExpenseInfo(
                        "testCategory" + j,
                        "testbrand" + j, random.nextInt(1000, 15001), (j == 1) ? null : UUID.randomUUID().toString(), random.nextBoolean(), tastes[random.nextInt(tastes.length)],
                        moods[random.nextInt(moods.length)], coffeeBeans[random.nextInt(coffeeBeans.length)], likeHates[random.nextInt(likeHates.length)],
                        random.nextInt(2), LocalDate.ofEpochDay(randomDay), memberInfo);
                expenseRepo.save(expense);
                if(random.nextBoolean()) {
                    postRepo.save(new PostInfo(UUID.randomUUID().toString(), expense));
                    postNumber++;
                }
            }
        }
    }

    @Test
    void 댓글_등록() {
        // given
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // 댓글 달 게시글
        PostInfo post = postRepo.findById(random.nextLong(postNumber)).get();
        // 댓글 다는 회원
        final String memberUid = UUID.randomUUID().toString();
        final String memberNickname = UUID.randomUUID().toString();
        MemberInfo member = memberRepo.save(new MemberInfo(AuthDomain.KAKAO, memberUid, memberNickname, null, null));
        // 댓글 내용
        final String commentContent = UUID.randomUUID().toString();
        CommentCreateDto createComment = new CommentCreateDto(commentContent, post.getId());

        // when
        CommentDto commentDto = commentService.create(createComment, member.getId());

        // then
        assertThat(commentDto.content()).isEqualTo(commentContent);
        assertThat(commentDto.nickname()).isEqualTo(memberNickname);
    }

    @Test
    void 댓글_수정() {
        // given
        ThreadLocalRandom random = ThreadLocalRandom.current();
        PostInfo post = postRepo.findById(random.nextLong(postNumber)).get();
        final String memberUid = UUID.randomUUID().toString();
        final String memberNickname = UUID.randomUUID().toString();
        MemberInfo member = memberRepo.save(new MemberInfo(AuthDomain.KAKAO, memberUid, memberNickname, null, null));
        final String commentContent = UUID.randomUUID().toString();
        CommentInfo comment = commentRepo.save(new CommentInfo(commentContent, member, post));
        final String modifiedContent = UUID.randomUUID().toString();
        CommentModifyDto commentModifyDto = new CommentModifyDto(modifiedContent);

        // when
        CommentDto modifiedComment = commentService.modify(comment.getId(), commentModifyDto, member.getId());

        // then
        assertThat(modifiedComment.id()).isEqualTo(comment.getId());
        assertThat(modifiedComment.content()).isEqualTo(modifiedContent);
        assertThat(modifiedComment.nickname()).isEqualTo(memberNickname);
    }

    @Test
    void 댓글_삭제() {
        // given
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // 댓글 달 게시글
        PostInfo post = postRepo.findById(random.nextLong(postNumber)).get();
        // 댓글 다는 회원
        final String memberUid = UUID.randomUUID().toString();
        final String memberNickname = UUID.randomUUID().toString();
        MemberInfo member = memberRepo.save(new MemberInfo(AuthDomain.KAKAO, memberUid, memberNickname, null, null));
        // 댓글 내용
        final String commentContent = UUID.randomUUID().toString();
        CommentInfo comment = commentRepo.save(new CommentInfo(commentContent, member, post));

        // when
        CommentDeleteDto deleteDto = commentService.delete(comment.getId(), member.getId());

        // then
        assertThat(deleteDto.id()).isEqualTo(comment.getId());
        assertThat(deleteDto.message()).isEqualTo("삭제되었습니다.");
        assertThat(commentRepo.existsById(deleteDto.id())).isFalse();

    }
}
