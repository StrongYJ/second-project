package com.secondproject.monthlycoffee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.comment.CommentCreateDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDeleteDto;
import com.secondproject.monthlycoffee.dto.comment.CommentDto;
import com.secondproject.monthlycoffee.dto.comment.CommentModifyDto;
import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;
import com.secondproject.monthlycoffee.repository.CommentInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import com.secondproject.monthlycoffee.repository.PostInfoRepository;

import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentInfoRepository commentRepo;
    private final MemberInfoRepository memberRepo;
    private final PostInfoRepository postRepo;
    
    public CommentDto create(CommentCreateDto comment, Long memberId) {
        MemberInfo member = memberRepo.findById(memberId).orElseThrow();
        PostInfo post = postRepo.findById(comment.postId()).orElseThrow();

        CommentInfo newComment = new CommentInfo(comment.content(), member, post);
        commentRepo.save(newComment);
        return new CommentDto(newComment);
    }

    public CommentDto modify(Long commentId, CommentModifyDto comment, Long memberId) {
        CommentInfo modifyingComment = commentRepo.findByIdAndMemberId(commentId, memberId).orElseThrow();
        modifyingComment.modifyContent(comment.content());;
        return new CommentDto(modifyingComment);
    }

    public CommentDeleteDto delete(Long commentId, Long memberId) {
        CommentInfo comment = commentRepo.findByIdAndMemberId(commentId, memberId).orElseThrow(() ->
                new IllegalArgumentException("????????? ????????? ???????????? ????????? ????????? ????????? ????????? ????????????."));

        commentRepo.delete(comment);
        return new CommentDeleteDto(commentId, "?????????????????????.");
    }
    
}
