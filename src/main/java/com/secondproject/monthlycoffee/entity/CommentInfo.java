package com.secondproject.monthlycoffee.entity;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentInfo extends BaseTime{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ci_id")
    private Long id;

    @Column(name = "ci_content")
    private String content;

    @JoinColumn(name = "ci_mi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;
    
    @JoinColumn(name = "ci_pi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostInfo post;

    public CommentInfo(String content, MemberInfo member, PostInfo post) {
        this.content = content;
        this.member = member;
        this.post = post;
    }
}
