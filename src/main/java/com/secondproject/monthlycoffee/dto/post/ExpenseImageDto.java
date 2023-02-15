package com.secondproject.monthlycoffee.dto.post;

import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExpenseImageDto(
    @Schema(description = "이미지 식별 번호", example = "1")
    Long id,
    @Schema(description = "이미지 이름", example = "coffee_1676432744505.jpg")
    String filename
) {
    public ExpenseImageDto(ExpenseImageInfo entity) {
        this(entity.getId(), entity.getFilename());
    }
}
