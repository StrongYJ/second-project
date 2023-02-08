package com.secondproject.monthlycoffee.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostInfo {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pi_id")
    private Long id;
    
    @Column(name = "pi_content", nullable = false)
    @Lob
    private String content;
    
    @Column(name = "pi_reg_dt", nullable = false)
    private LocalDateTime regDt;
    
    @Column(name = "pi_update_dt")
    private LocalDateTime updateDt;
    
    @JoinColumn(name = "pi_bi_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BudgetInfo budget;
}
