package com.secondproject.monthlycoffee.dto.postlike;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {
    @Schema(description = "좋아요한 게시물 식별번호")
    private Long postId;
    @Schema(description = "좋아요됐다면 true 취소됐다면 false", example = "true")
    private Boolean isLiked;
}
