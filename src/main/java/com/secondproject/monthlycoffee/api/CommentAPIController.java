package com.secondproject.monthlycoffee.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.comment.CommentCreateDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDeleteDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDto;
import com.secondproject.monthlycoffee.dto.comment.CommentModifyDto;
import com.secondproject.monthlycoffee.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "댓글 API", description = "댓글 작성, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentAPIController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("")
    public ResponseEntity<CommentDto> postCreateComment(
        @Parameter(description = "회원 식별 번호")
        @RequestParam("memberId") Long memberId,
        @RequestBody @Validated CommentCreateDto comment
    ) {
        CommentDto newComment = commentService.create(comment, memberId);
        return new ResponseEntity<CommentDto>(newComment, HttpStatus.CREATED);
    }


    @Operation(summary = "댓글 수정")
    @PutMapping("/{comment-id}")
    public ResponseEntity<CommentDto> putModifyComment(
        @Parameter(description = "수정할 댓글 식별 번호")
        @PathVariable("comment-id") Long commentId,
        @Parameter(description = "회원 식별 번호")
        @RequestParam("memberId") Long memberId,
        @RequestBody @Validated CommentModifyDto comment
    ) {
        CommentDto modifiedComment = commentService.modify(commentId, comment, memberId);
        return new ResponseEntity<>(modifiedComment, HttpStatus.OK);
    }
    
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<CommentDeleteDto> deleteComment(
        @Parameter(description = "삭제할 댓글 식별 번호")
        @PathVariable("comment-id") Long commentId,
        @Parameter(description = "회원 식별 번호")
        @RequestParam("memberId") Long memberId
    ) {
        CommentDeleteDto deletedComment = commentService.delete(commentId, memberId);
        return new ResponseEntity<>(deletedComment, HttpStatus.OK);
    }
}
