package com.secondproject.monthlycoffee.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeletePost(
    @Schema(description = "삭제된 게시글 식별 번호")
    Long id,
    @Schema(description = "삭제 메세지")
    String message
) {
    
}
