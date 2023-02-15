package com.secondproject.monthlycoffee.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.secondproject.monthlycoffee.dto.postlike.PostLikeDto;
import com.secondproject.monthlycoffee.entity.LovePostInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.repository.LovePostInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final LovePostInfoRepository likePostRepo;
    private final MemberInfoRepository memberRepo;
    private final PostInfoRepository postRepo;
    
    @Transactional
    public PostLikeDto likePost(Long postId, Long memberId) {
        PostInfo post = postRepo.findById(postId).orElseThrow();
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        PostLikeDto postLikeDto = new PostLikeDto(postId, null);
        
        likePostRepo.findByPostAndMember(post, member).ifPresentOrElse(
            v -> {
                likePostRepo.delete(v);
                postLikeDto.setIsLiked(false);
            }, 
            () -> {
                likePostRepo.save(new LovePostInfo(member, post));
                postLikeDto.setIsLiked(true);
            }
        );
        
        if(postLikeDto.getIsLiked() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "postLikeDto의 isLiked는 null일 수 없습니다.");
        }

        return postLikeDto;
    }


}
