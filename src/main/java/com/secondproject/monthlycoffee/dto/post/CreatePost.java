package com.secondproject.monthlycoffee.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreatePost(
    @Schema(description = " 게시글 등록할 내용")
    @NotBlank
    String content
) {
    
}
