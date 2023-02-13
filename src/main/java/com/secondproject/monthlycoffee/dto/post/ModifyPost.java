package com.secondproject.monthlycoffee.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ModifyPost(
    @Schema(description = "내용")
    @NotBlank
    String content
    ) {
    
}
