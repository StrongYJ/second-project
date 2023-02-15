package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.dto.post.PostCreateDto;
import com.secondproject.monthlycoffee.dto.post.PostDeleteDto;
import com.secondproject.monthlycoffee.dto.post.PostDetailDto;
import com.secondproject.monthlycoffee.dto.post.PostModifyDto;
import com.secondproject.monthlycoffee.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 API", description = "게시글 조회, 작성, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostAPIController {
    
    private final PostService postService;

    @Operation(summary = "전체 게시글 조회")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<PostBasicDto>> getPostBasics(@Parameter(hidden = true) Pageable pageable) {
        return new ResponseEntity<Page<PostBasicDto>>(postService.getAllPost(pageable), HttpStatus.OK);
    }


    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{post-id}")
    public ResponseEntity<PostDetailDto> getPostDetail(
        @Parameter(description = "게시물 식별 번호") @PathVariable("post-id") Long id
    ) {
        return new ResponseEntity<PostDetailDto>(postService.getPostDetail(id), HttpStatus.OK);
    }
    
    @Operation(summary = "게시글 등록")
    @PostMapping("")
    public ResponseEntity<PostDetailDto> postCreatePost(
        @RequestBody @Validated PostCreateDto createPost,
        @Parameter(description = "회원 식별 번호")
        @RequestParam("memberId") 
        Long memberId
    ) {
        PostDetailDto createdPost = postService.create(createPost, memberId);
        return new ResponseEntity<PostDetailDto>(createdPost, HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{post-id}")
    public ResponseEntity<PostDetailDto> putModifyPost(
        @Parameter(description = "게시글 식별 번호")
        @PathVariable("post-id") Long id,
        @Parameter(description = "게시글을 작성한 회원 식별 번호")
        @RequestParam("memberId") Long memberId,
        @RequestBody @Validated PostModifyDto modifyPost
    ) {
        PostDetailDto modifiedPost = postService.modify(id, memberId, modifyPost);
        return new ResponseEntity<>(modifiedPost, HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{post-id}")
    public ResponseEntity<PostDeleteDto> deletePost(
        @Parameter(description = "게시글 식별 번호")
        @PathVariable("post-id") Long id,
        @Parameter(description = "게시글을 작성한 회원 식별 번호")
        @RequestParam("memberId") Long memberId
    ) {
        PostDeleteDto deletedPost = postService.delete(id, memberId);
        return new ResponseEntity<>(deletedPost, HttpStatus.OK);
    }

}
