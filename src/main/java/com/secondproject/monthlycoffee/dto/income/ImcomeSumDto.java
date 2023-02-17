package com.secondproject.monthlycoffee.dto.income;

import java.time.LocalDate;
import java.time.YearMonth;

import lombok.Data;

public interface ImcomeSumDto {
    String getYearMonth();
    Integer getSum();
    
}
