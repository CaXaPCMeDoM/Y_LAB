package com.y_lab.y_lab.entity;

import com.y_lab.y_lab.entity.enums.CarState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private Long carId;
    private String brand;
    private String model;
    private int year;
    private double price;
    private CarState state;
}
