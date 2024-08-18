package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.CarState;

public record CarResponseDto(
        Long carId,
        String brand,
        String model,
        int year,
        double price,
        CarState state
) {
}
