package com.secondproject.monthlycoffee.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.postlike.PostLikeDto;
import com.secondproject.monthlycoffee.service.LikePostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postlikes")
public class PostLikesAPIController {
    
    private final LikePostService likePostService;

    @Operation(summary = "게시글을 좋아요합니다.", description = "이미 좋아요한 상태라면 좋아요가 취소됩니다.")
    @PostMapping("/{post-id}")
    public ResponseEntity<PostLikeDto> postLikePost(
        @Parameter(description = "좋아요(취소)할 게시물 식별번호")
        @PathVariable("post-id") Long postId,
        @Parameter(description = "로그인한 회원 식별 번호")
        @RequestParam("memberId") Long memberId
    ) {
        return new ResponseEntity<>(likePostService.likePost(postId, memberId), HttpStatus.OK);
    }
}