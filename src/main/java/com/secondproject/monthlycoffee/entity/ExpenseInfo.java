package com.secondproject.monthlycoffee.entity;

import java.time.LocalDate;

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
public class ExpenseInfo extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ei_id")
    private Long id;
    
    @Column(name = "ei_category")
    private String category;

    @Column(name = "ei_brand")
    private String brand;

    @Column(name = "ei_price")
    private Integer price;

    @Column(name = "ei_memo")
    private String memo;

    @Column(name = "ei_tumbler")
    private Integer tumbler;

    @Column(name = "ei_taste")
    private Integer taste;

    @Column(name = "ei_mood")
    private Integer mood;

    @Column(name = "ei_bean")
    private Integer bean;

    @Column(name = "ei_like")
    private Integer likeHate;

    @Column(name = "ei_payment")
    private Integer payment;

    @Column(name = "ei_date")
    private LocalDate date;

    @JoinColumn(name = "ei_mi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;
}
