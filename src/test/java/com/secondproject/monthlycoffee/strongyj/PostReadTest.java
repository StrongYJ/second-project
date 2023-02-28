package com.secondproject.monthlycoffee.strongyj;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.secondproject.monthlycoffee.entity.type.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.dto.post.PostDetailDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.repository.ExpenseImageInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;
import com.secondproject.monthlycoffee.service.PostService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@TestInstance(Lifecycle.PER_CLASS)
public class PostReadTest {
    
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private ExpenseInfoRepository expenseRepo;
    @Autowired private ExpenseImageInfoRepository expenseImageInfoRepo;
    @Autowired private PostInfoRepository postRepo;
    @Autowired private PostService postService;
    
    private long postNumber = 0;

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
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        // Random random = new Random();
        
        for(int i = 0; i < 20; i++) {
            MemberInfo member = new MemberInfo(
                    AuthDomain.KAKAO,
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), 
                LocalDate.ofEpochDay(randomDay), genders[random.nextInt(genders.length)]
            );
            memberRepo.save(member);
            members.add(member);
        }
        for(int i = 0; i < 100; i++) {
            for(int j = 0; j < random.nextInt(5); j++) {
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
    void 댓글_좋아요_이미지_없는_전체_게시글_조회() {
        Page<PostBasicDto> allPost = postService.getAllPost(Pageable.unpaged());
        Assertions.assertThat(allPost.getTotalElements()).isEqualTo(postNumber);
        Assertions.assertThat(allPost.getContent().get(new Random().nextInt(allPost.getContent().size())).commentNumber()).isEqualTo(0L);
        Assertions.assertThat(allPost.getContent().get(new Random().nextInt(allPost.getContent().size())).likeNumber()).isEqualTo(0L);
        Assertions.assertThat(allPost.getContent().get(new Random().nextInt(allPost.getContent().size())).images().size()).isEqualTo(0L);
        Assertions.assertThat(allPost.getContent().get(new Random().nextInt(allPost.getContent().size())).images()).isNotNull();
    }

    @Test
    @Transactional
    void 댓글_좋아요_이미지_없는_게시글_상세_조회() {
        List<PostInfo> posts = postRepo.findAll();
        PostInfo postInfo = posts.get(ThreadLocalRandom.current().nextInt(posts.size()));
        PostDetailDto postDetail = postService.getPostDetail(postInfo.getId(), null);
        Assertions.assertThat(postDetail).isEqualTo(new PostDetailDto(postInfo));
    }
}
