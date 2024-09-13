package com.y_lab.y_lab.entity.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderCreateRequestDto(
        @NotNull
        Long carId,
        @NotNull
        Long customerId
) {
}
