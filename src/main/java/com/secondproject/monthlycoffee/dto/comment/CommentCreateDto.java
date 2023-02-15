package com.secondproject.monthlycoffee.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateDto(
    @Schema(description = "댓글 내용") @NotBlank String content, 
    @Schema(description = "게시글 식별 번호") @NotBlank Long postId
) {
    
}
