package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.service.PostService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostAPIController {
    
    private final PostService postService;

    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<PostBasicDto>> getPostBasics(@Parameter(hidden = true) Pageable pageable) {
        return new ResponseEntity<Page<PostBasicDto>>(postService.getAllPost(pageable), HttpStatus.OK);
    }
}
