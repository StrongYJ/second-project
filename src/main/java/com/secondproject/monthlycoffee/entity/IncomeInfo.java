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
public class IncomeInfo extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ii_id", nullable = false) 
    private Long id;
    
    @Column(name = "ii_amount", nullable = false) 
    private Integer amount;
    
    @Column(name = "ii_note") 
    private String note;
    
    @JoinColumn(name = "ii_mi_id", nullable = false) 
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;
}
