package com.y_lab.y_lab.entity.dto.request;

import com.y_lab.y_lab.entity.enums.OrderStatus;

import javax.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Long orderId,
        @NotNull
        OrderStatus orderStatus
) {
}
