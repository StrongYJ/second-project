package com.secondproject.monthlycoffee.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private Boolean tumbler;

    @Column(name = "ei_taste")
    @Enumerated(EnumType.STRING)
    private Taste taste;

    @Column(name = "ei_mood")
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(name = "ei_bean")
    @Enumerated(EnumType.STRING)
    private CoffeeBean bean;

    @Column(name = "ei_likehate")
    @Enumerated(EnumType.STRING)
    private LikeHate likeHate;

    @Column(name = "ei_payment")
    private Integer payment;

    @Column(name = "ei_date")
    private LocalDate date;

    @JoinColumn(name = "ei_mi_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;

    @OneToMany(mappedBy = "expense")
    private List<ExpenseImageInfo> expenseImages = new ArrayList<>();

    public ExpenseInfo(String category, String brand, Integer price, String memo, Boolean tumbler, Taste taste,
            Mood mood, CoffeeBean bean, LikeHate likeHate, Integer payment, LocalDate date, MemberInfo member) {
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.memo = memo;
        this.tumbler = tumbler;
        this.taste = taste;
        this.mood = mood;
        this.bean = bean;
        this.likeHate = likeHate;
        this.payment = payment;
        this.date = date;
        this.member = member;
    }

    public void deleteMember() {
        this.member = null;
    }
    
}
