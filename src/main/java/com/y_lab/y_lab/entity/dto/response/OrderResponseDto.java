package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.OrderStatus;

import java.sql.Timestamp;

public record OrderResponseDto(
        Long id,
        Long carId,
        Long userId,
        OrderStatus status,
        Timestamp orderDate
) {
}
