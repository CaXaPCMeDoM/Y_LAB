package com.y_lab.y_lab.entity.dto.request;

import com.y_lab.y_lab.entity.enums.OrderStatus;

public record UpdateOrderStatusRequestDto(
        Long orderId,
        OrderStatus orderStatus
) {
}
