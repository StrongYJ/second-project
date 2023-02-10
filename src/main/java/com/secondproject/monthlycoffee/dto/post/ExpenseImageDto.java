package com.secondproject.monthlycoffee.dto.post;

import com.secondproject.monthlycoffee.entity.ExpenseImageInfo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExpenseImageDto(
    @Schema(description = "이미지 식별 번호")
    Long id, 
    @Schema(description = "이미지 이름")
    String imageName
) {
    public ExpenseImageDto(ExpenseImageInfo entity) {
        this(entity.getId(), entity.getUri());
    }
}
