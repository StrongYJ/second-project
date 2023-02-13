package com.secondproject.monthlycoffee.dto.comment;

import java.time.LocalDateTime;
import java.util.Optional;

import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentDto(
    @Schema(description = "댓글 식별 번호")
    Long id, 
    @Schema(description = "댓글 내용")
    String content, 
    @Schema(description = "댓글 작성자 닉네임")
    String nickname,
    @Schema(description = "댓글 작성일")
    LocalDateTime createDt,
    @Schema(description = "댓글 수정일")
    LocalDateTime updateDt
) {
    public CommentDto(CommentInfo entity) {
        this(
            entity.getId(), entity.getContent(), 
            Optional.ofNullable(entity.getMember())
                .map(MemberInfo::getNickname)
                .orElse(null), 
            entity.getCreateDt(), entity.getUpdateDt()
        );
    }    

}
