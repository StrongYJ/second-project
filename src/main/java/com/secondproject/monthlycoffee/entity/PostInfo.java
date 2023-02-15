package com.secondproject.monthlycoffee.entity;

import java.util.ArrayList;
import java.util.List;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostInfo extends BaseTime{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pi_id")
    private Long id;
    
    @Column(name = "pi_content", nullable = false, columnDefinition = "text")
    private String content;
    
    @JoinColumn(name = "pi_ei_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ExpenseInfo expense;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<CommentInfo> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<LovePostInfo> likes = new ArrayList<>();

    public PostInfo(String content, ExpenseInfo expense) {
        this.content = content;
        this.expense = expense;
    }

    public void modifyContent(String content) {
        this.content = content;
    }
}
