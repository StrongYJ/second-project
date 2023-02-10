package com.secondproject.monthlycoffee.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.secondproject.monthlycoffee.dto.post.PostBasicDto;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostInfoRepository postRepo;

    public Page<PostBasicDto> getAllPost(Pageable pageable) {
        return postRepo.findAll(pageable).map(PostBasicDto::new);
    }
}
