package com.secondproject.monthlycoffee.token;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponseDto(
        @Schema(description = "메시지", example = "액세스 토큰이 재발급되었습니다") String message,
        @Schema(description = "상태", example = "true") boolean status
) {
}
