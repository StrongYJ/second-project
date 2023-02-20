package com.secondproject.monthlycoffee.strongyj;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondproject.monthlycoffee.dto.post.PostCreateDto;
import com.secondproject.monthlycoffee.dto.post.PostModifyDto;
import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.LovePostInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;
import com.secondproject.monthlycoffee.repository.CommentInfoRepository;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.LovePostInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PostCudTest {
    
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private PostInfoRepository postRepo;
    @Autowired private ExpenseInfoRepository expenseRepo;
    @Autowired private CommentInfoRepository commentRepo;
    @Autowired private LovePostInfoRepository LikeRepo;
    @Autowired private EntityManager em;

    private MemberInfo member;
    private List<MemberInfo> members = new ArrayList<>();
    private ExpenseInfo expense;

    @BeforeEach
    void createDummyMembers() {
        for(int i = 0; i < 20; i++) {
            MemberInfo newMember = new MemberInfo(UUID.randomUUID().toString(), "test" + (i + 1), LocalDate.now(), Gender.NONE);
            members.add(newMember);
            memberRepo.save(newMember);
        }
        member = new MemberInfo("test", "test", LocalDate.now(), Gender.FEMALE);
        memberRepo.save(member);
        expense = new ExpenseInfo("라떼", "스타벅스", 6000, null, false, Taste.SWEET, Mood.WORK, CoffeeBean.COLOMBIA, LikeHate.LIKE, 0, LocalDate.now(), member);
        expenseRepo.save(expense);

    }

    @Test
    void createNormal() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        mockMvc.perform(
            post("/api/posts").param("memberId", member.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createPost)
        ).andExpect(status().isCreated())
        .andDo(print());
    }

    @Test
    void createInvalidMember() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        mockMvc.perform(
            post("/api/posts").param("memberId", members.get(5).getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createPost)
        ).andExpect(status().isNotFound())
        .andExpect(content().string(containsString("NoSuchElementException")))
        .andDo(print());
    }

    @Test
    void createDuplicateExpense() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        mockMvc.perform(
                post("/api/posts").param("memberId", member.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPost))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(
                post("/api/posts").param("memberId", member.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPost))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("IllegalArgumentException")))
                .andDo(print());
    }

    @Test
    void modifyNormal() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        MvcResult mockReturn = mockMvc.perform(
            post("/api/posts").param("memberId", member.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createPost)
        ).andExpect(status().isCreated())
        .andReturn();

        Map<String, String> createdPost = objectMapper.readValue(mockReturn.getResponse().getContentAsString(), Map.class);
        long postId = Long.parseLong(String.valueOf(createdPost.get("id")));
        mockMvc.perform(
                put("/api/posts/" + postId).param("memberId", member.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PostModifyDto("맛없다.")))
                ).andExpect(status().isOk());

        String content = postRepo.findById(postId).get().getContent();
        Assertions.assertEquals("맛없다.", content);
    }
    
    @Test
    void modifyInvalidMember() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        MvcResult mockReturn = mockMvc.perform(
            post("/api/posts").param("memberId", member.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(createPost)
        ).andExpect(status().isCreated())
        .andReturn();

        Map<String, String> createdPost = objectMapper.readValue(mockReturn.getResponse().getContentAsString(), Map.class);
        long postId = Long.parseLong(String.valueOf(createdPost.get("id")));
        mockMvc.perform(
                put("/api/posts/" + postId).param("memberId", members.get(3).getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PostModifyDto("맛없다.")))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("IllegalArgumentException")));

    }

    @Test
    void deleteNormal() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        MvcResult mockReturn = mockMvc.perform(
                post("/api/posts").param("memberId", member.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPost))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, String> createdPost = objectMapper.readValue(mockReturn.getResponse().getContentAsString(), Map.class);
        long postId = Long.parseLong(String.valueOf(createdPost.get("id")));

        mockMvc.perform(delete("/api/posts/" + postId).param("memberId", member.getId().toString()))
            .andExpect(status().isOk())
            .andDo(print());
        
        Assertions.assertFalse(postRepo.existsById(postId));
    }

    @Test
    void deletePostHavingCommentsAndLikes() throws Exception {
        Assertions.assertEquals(20, members.size());

        ObjectMapper objectMapper = new ObjectMapper();
        String createPost = objectMapper.writeValueAsString(new PostCreateDto(expense.getId(), "맛있다."));
        MvcResult mockReturn = mockMvc.perform(
                post("/api/posts").param("memberId", member.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPost))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, String> createdPost = objectMapper.readValue(mockReturn.getResponse().getContentAsString(), Map.class);
        final long postId = Long.parseLong(String.valueOf(createdPost.get("id")));

        PostInfo post = postRepo.findById(postId).get();

        for(var dummyMember : members) {
            commentRepo.save(new CommentInfo(UUID.randomUUID().toString(), dummyMember, post));
            LikeRepo.save(new LovePostInfo(dummyMember, post));
        }

        long commentsNumber =  em.createQuery("select count(c) from CommentInfo c where c.post = :post", Long.class).setParameter("post", post).getSingleResult();
        long LovePostInfoNumber =  em.createQuery("select count(l) from LovePostInfo l where l.post = :post", Long.class).setParameter("post", post).getSingleResult();


        mockMvc.perform(delete("/api/posts/" + postId).param("memberId", member.getId().toString()))
            .andExpect(status().isOk())
            .andDo(print());

        commentsNumber = em.createQuery("select count(c) from CommentInfo c where c.post = :post", Long.class)
                .setParameter("post", post).getSingleResult();
        LovePostInfoNumber = em.createQuery("select count(l) from LovePostInfo l where l.post = :post", Long.class)
                .setParameter("post", post).getSingleResult();        

        Assertions.assertEquals(0, commentsNumber);
        Assertions.assertEquals(0, LovePostInfoNumber);
        Assertions.assertFalse(postRepo.existsById(postId));
    }

}
