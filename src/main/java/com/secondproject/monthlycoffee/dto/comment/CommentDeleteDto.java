package com.secondproject.monthlycoffee.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentDeleteDto(
    @Schema(description = "삭제된 댓글 식별 번호")
    Long id,
    @Schema(description = "삭제 메세지", defaultValue = "삭제되었습니다.")
    String message
) {
    
}
