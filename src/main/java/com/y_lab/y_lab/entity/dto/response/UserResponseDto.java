package com.y_lab.y_lab.entity.dto.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserResponseDto(
        @NotNull
        Long userId,
        @NotBlank
        String username
) {
}
