package com.secondproject.monthlycoffee.doyouee.dto;

import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearMonthTestDto {
    private YearMonth yearMonth;
    private int sum;

    public void sumCalc(int number) {
        sum += number;
    }
}
