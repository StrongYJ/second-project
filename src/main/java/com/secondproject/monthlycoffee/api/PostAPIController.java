package com.secondproject.monthlycoffee.api;

import com.secondproject.monthlycoffee.config.security.AuthMember;
import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.dto.AuthDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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

    @SecurityRequirements
    @Operation(summary = "전체 게시글 조회")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<PostBasicDto>> getPostBasics(
            @Parameter(hidden = true)
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return new ResponseEntity<>(postService.getAllPost(pageable), HttpStatus.OK);
    }


    @SecurityRequirements
    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{post-id}")
    public ResponseEntity<PostDetailDto> getPostDetail(
        @Parameter(description = "게시물 식별 번호") @PathVariable("post-id") Long id,
        HttpServletRequest request
    ) {
        String bearerAccess = request.getHeader(HttpHeaders.AUTHORIZATION);
        return new ResponseEntity<>(postService.getPostDetail(id, bearerAccess), HttpStatus.OK);
    }
    
    @Operation(summary = "게시글 등록")
    @PostMapping("")
    public ResponseEntity<PostDetailDto> postCreatePost(
            @RequestBody @Validated PostCreateDto createPost,
            @AuthMember AuthDto authDto
            ) {
        PostDetailDto createdPost = postService.create(createPost, authDto.id());
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{post-id}")
    public ResponseEntity<PostDetailDto> putModifyPost(
        @Parameter(description = "게시글 식별 번호")
        @PathVariable("post-id") Long id,
        @RequestBody @Validated PostModifyDto modifyPost,
        @AuthMember AuthDto authDto
    ) {
        PostDetailDto modifiedPost = postService.modify(id, authDto.id(), modifyPost);
        return new ResponseEntity<>(modifiedPost, HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{post-id}")
    public ResponseEntity<PostDeleteDto> deletePost(
        @Parameter(description = "게시글 식별 번호")
        @PathVariable("post-id") Long id,
        @AuthMember AuthDto authDto
    ) {
        PostDeleteDto deletedPost = postService.delete(id, authDto.id());
        return new ResponseEntity<>(deletedPost, HttpStatus.OK);
    }

}
