package com.y_lab.y_lab.entity.dto.request;

public record OrderCreateRequestDto(
        Long carId,
        Long customerId
) {
}
