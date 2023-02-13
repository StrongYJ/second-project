package com.secondproject.monthlycoffee.dto.expense;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private String category;
    private String brand;
    private Integer price;
    private String memo;
    private Boolean tumbler;
    private Taste taste;
    private Mood mood;
    private CoffeeBean bean;
    private LikeHate likeHate;
    private Integer payment;
    private LocalDate date;
}
