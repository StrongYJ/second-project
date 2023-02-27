package com.secondproject.monthlycoffee.error;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "에러 종류")
        String error,
        @Schema(description = "에러 메세지")
        String message) {
    
}
