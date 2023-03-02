package com.secondproject.monthlycoffee.strongyj.service;

import com.secondproject.monthlycoffee.dto.comment.CommentCreateDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.*;
import com.secondproject.monthlycoffee.service.CommentService;
import com.secondproject.monthlycoffee.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = {CommentInfoRepository.class, MemberInfoRepository.class, PostInfoRepository.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private ExpenseInfoRepository expenseRepo;
    @Autowired private ExpenseImageInfoRepository expenseImageInfoRepo;
    @Autowired private PostInfoRepository postRepo;
    @Autowired private PostService postService;
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
        PostInfo post = postRepo.findById(random.nextLong(postNumber)).get();
        String memberUid = UUID.randomUUID().toString();
        MemberInfo member = memberRepo.save(new MemberInfo(AuthDomain.KAKAO, memberUid, null, null, null));
        CommentCreateDto createComment = new CommentCreateDto("test", post.getId());

        // when



    }
}
