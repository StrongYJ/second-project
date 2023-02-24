package com.secondproject.monthlycoffee.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface TumblerRankCreate {
    Long getId();
    String getNickname();
    Integer getCountUse();
}
