package com.secondproject.monthlycoffee.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentModifyDto(
    @Schema(description = "댓글 내용") @NotBlank String content
) {
    
}
