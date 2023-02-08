package com.secondproject.monthlycoffee.entity;

import java.time.LocalDateTime;
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
public class CommentInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ci_id")
    private Long id;

    @Column(name = "ci_content")
    private String content;

    @Column(name = "ci_reg_dt")
    private LocalDateTime regDt;

    @Column(name = "ci_update_dt")
    private LocalDateTime updateDt;

    @JoinColumn(name = "ci_mi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;
    
    @JoinColumn(name = "ci_bi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BudgetInfo budget;
}
