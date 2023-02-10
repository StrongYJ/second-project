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
public class BudgetInfo extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bi_id", nullable = false) 
    private Long id;

    @Column(name = "bi_amount", nullable = false) 
    private Integer amount;

    @JoinColumn(name = "bi_mi_id", nullable = false) 
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;

    public BudgetInfo(Integer amount, MemberInfo member) {
        this.amount = amount;
        this.member = member;
    }

}
