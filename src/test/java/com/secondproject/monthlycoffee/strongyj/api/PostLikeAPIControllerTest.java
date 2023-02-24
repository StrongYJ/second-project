package com.secondproject.monthlycoffee.strongyj.api;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.LovePostInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class PostLikeAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberInfoRepository memberRepo;
    @Autowired
    private ExpenseInfoRepository expenseInfoRepository;
    @Autowired
    private PostInfoRepository postInfoRepository;
    @Autowired
    private LovePostInfoRepository lovePostRepo;
    @Autowired
    private JwtUtil jwtUtil;

    private List<MemberInfo> dummyMembers = new ArrayList<>();
    private List<ExpenseInfo> dummyExpense = new ArrayList<>();
    private List<PostInfo> dummyPosts = new ArrayList<>();

    private long memberId;
    @BeforeEach
    void init() {
        RestAssured.port = port;

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
    }

    @Test
    void 게시글_좋아요_싫어요() {
        MemberInfo member = memberRepo.findById(memberId).get();
        PostInfo likePost = dummyPosts.get(ThreadLocalRandom.current().nextInt(dummyPosts.size()));
        String access = jwtUtil.createAccess(memberId, JwtProperties.ACCESS_EXPIRATION_TIME);

        Assertions.assertThat(lovePostRepo.findByPostAndMember(likePost, member).isPresent()).isFalse();

        given()
                .header(HttpHeaders.AUTHORIZATION, access)
                .when()
                .post("/api/postlikes/" + likePost.getId())
                .then().log().all()
                .statusCode(200)
                .body("postId", equalTo(likePost.getId().intValue()))
                .body("isLiked", equalTo(true));

        Assertions.assertThat(lovePostRepo.findByPostAndMember(likePost, member).isPresent()).isTrue();

        given()
                .header(HttpHeaders.AUTHORIZATION, access)
                .when()
                .post("/api/postlikes/" + likePost.getId())
                .then().log().all()
                .statusCode(200)
                .body("postId", equalTo(likePost.getId().intValue()))
                .body("isLiked", equalTo(false));

        Assertions.assertThat(lovePostRepo.findByPostAndMember(likePost, member).isPresent()).isFalse();
    }

}
