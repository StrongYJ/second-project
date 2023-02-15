package com.secondproject.monthlycoffee.dto.post;

import java.util.List;

import com.secondproject.monthlycoffee.entity.PostInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostBasicDto(
    @Schema(description = "게시글 식별 번호")
    Long id, 
    @Schema(description = "이미지 정보")
    List<ExpenseImageDto> images,
    @Schema(description = "게시글 좋아요 수")
    Long likeNumber, 
    @Schema(description = "게시글 댓글 수")
    Long commentNumber
) {
    public PostBasicDto(PostInfo entity, Long likeNumber, Long commentNumber) {
        this(
            entity.getId(), 
            entity.getExpense().getExpenseImages().stream().map(ExpenseImageDto::new).toList(),
            likeNumber, 
            commentNumber
        );
    }
}
