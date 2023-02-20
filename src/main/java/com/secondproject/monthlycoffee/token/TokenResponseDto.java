package com.secondproject.monthlycoffee.token;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponseDto(
        @Schema(description = "메시지") String message,
        @Schema(description = "상태") boolean status
) {
}
