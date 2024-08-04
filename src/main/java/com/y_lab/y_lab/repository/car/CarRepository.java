package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.entity.Car;

import java.util.List;
import java.util.Map;

public interface CarRepository {
    Long add(Car car);

    Car delete(Long id);

    List<Car> findAll();

    boolean editCar(Long id, Car car);

    List<Car> findByBrand(String brand);

    List<Car> findByModel(String model);

    List<Car> findByYear(int year);

    List<Car> findByPriceRange(double minPrice, double maxPrice);
}

