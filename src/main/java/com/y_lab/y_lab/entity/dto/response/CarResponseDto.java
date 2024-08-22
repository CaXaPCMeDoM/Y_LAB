package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.CarState;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
