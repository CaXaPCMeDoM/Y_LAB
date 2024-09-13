package com.y_lab.y_lab.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponseDto(
        @NotNull
        Long userId,
        @NotBlank
        String username
) {
}
