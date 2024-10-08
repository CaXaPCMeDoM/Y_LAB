package com.y_lab.y_lab.entity;

import com.y_lab.y_lab.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long carId;
    private Long userId;
    private OrderStatus status;
    private Timestamp orderDate;
}
