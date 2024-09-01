package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record OrderResponseDto(
        @NotNull
        Long id,
        @NotNull
        Long carId,
        @NotNull
        Long userId,
        @NotNull
        OrderStatus status,
        @NotNull
        Timestamp orderDate
) {
}
