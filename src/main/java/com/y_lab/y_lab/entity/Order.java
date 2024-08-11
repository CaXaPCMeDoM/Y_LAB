package com.y_lab.y_lab.entity;

import com.y_lab.y_lab.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private Long carId;
    private Long userId;
    private OrderStatus status;
    private Timestamp orderDate;
}
