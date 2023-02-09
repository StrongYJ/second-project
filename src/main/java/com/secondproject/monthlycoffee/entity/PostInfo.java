package com.secondproject.monthlycoffee.entity;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;

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
public class PostInfo extends BaseTime{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pi_id")
    private Long id;
    
    @Column(name = "pi_content", nullable = false)
    @Lob
    private String content;
    
    @JoinColumn(name = "pi_bi_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BudgetInfo budget;


}
