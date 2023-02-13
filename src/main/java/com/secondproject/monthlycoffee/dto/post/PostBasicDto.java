package com.secondproject.monthlycoffee.dto.post;

import java.util.List;

import com.secondproject.monthlycoffee.entity.PostInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostBasicDto(
    @Schema(description = "게시글 식별 번호")
    Long id, 
    @Schema(description = "게시글 좋아요 수")
    Integer likeNumber, 
    @Schema(description = "게시글 댓글 수")
    Integer commentNumber,
    List<ExpenseImageDto> images
) {
    public PostBasicDto(PostInfo entity) {
        this(
            entity.getId(), 
            entity.getLikes().stream().filter(l -> l.getChoice() == 1).toList().size(), 
            entity.getComments().size(), 
            entity.getExpense().getExpenseImages().stream().map(ExpenseImageDto::new).toList()
        );
    }
}
