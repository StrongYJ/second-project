package com.secondproject.monthlycoffee.dto.post;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.secondproject.monthlycoffee.dto.comment.CommentDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostDetailDto {
    @Schema(description = "게시글 식별 번호")
    private Long id;
    @Schema(description = "게시글 작성자 닉네임")
    private String nickname;
    @Schema(description = "커피 메뉴 종류")
    private String category;
    @Schema(description = "커피 브랜드")
    private String brand;
    @Schema(description = "커피 맛")
    private Taste taste;
    @Schema(description = "커피 분위기")
    private Mood mood;
    @Schema(description = "커피  원두")
    private CoffeeBean bean;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "게시글 좋아요 여부")
    private Boolean isLiked = false;

    private List<ExpenseImageDto> images;
    private List<CommentDto> comments;
    
    public PostDetailDto(PostInfo entity) {
        ExpenseInfo expense = entity.getExpense();
        MemberInfo member = expense.getMember();

        this.id = entity.getId();
        this.nickname = ObjectUtils.isEmpty(member) ? null : member.getNickname();
        this.category = expense.getCategory();
        this.brand = expense.getBrand();
        this.taste = expense.getTaste();
        this.mood = expense.getMood();
        this.bean = expense.getBean();
        this.content = entity.getContent();
        this.images = expense.getExpenseImages().stream().map(ExpenseImageDto::new).toList();
        this.comments = entity.getComments().stream().map(CommentDto::new).toList();
    }
    public PostDetailDto(PostInfo entity, Boolean isLiked) {
        ExpenseInfo expense = entity.getExpense();
        MemberInfo member = expense.getMember();

        this.id = entity.getId();
        this.nickname = ObjectUtils.isEmpty(member) ? null : member.getNickname();
        this.category = expense.getCategory();
        this.brand = expense.getBrand();
        this.taste = expense.getTaste();
        this.mood = expense.getMood();
        this.bean = expense.getBean();
        this.content = entity.getContent();
        this.isLiked = isLiked;
        this.images = expense.getExpenseImages().stream().map(ExpenseImageDto::new).toList();
        this.comments = entity.getComments().stream().map(CommentDto::new).toList();
    }
}
