package com.secondproject.monthlycoffee.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TumblerRank {
    private Long id;
    private String nickname;
    private Integer useTumbler;
    private Integer rank;
    private String grade;
}
