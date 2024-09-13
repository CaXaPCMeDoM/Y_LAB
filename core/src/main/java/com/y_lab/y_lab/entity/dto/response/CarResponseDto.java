package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.CarState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarResponseDto(
        @NotNull
        Long carId,
        @NotBlank(message = "Brand cannot be null")
        String brand,
        @NotBlank
        String model,
        @Min(value = 1800)
        int year,
        @Min(value = 0)
        double price,
        @NotNull
        CarState state
) {
}
