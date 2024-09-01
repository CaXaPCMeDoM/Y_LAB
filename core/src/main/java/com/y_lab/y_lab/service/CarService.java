package com.y_lab.y_lab.service;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.repository.car.CarRepository;
import entity.ActionType;
import org.spring.boot.starter.my.annotation.Auditable;
import org.springframework.stereotype.Service;
import y_lab.annotaion.Loggable;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Auditable(action_type = ActionType.ADD_CAR)
    @Loggable
    public void addCar(Car car) {
        try {
            carRepository.add(car);
        } catch (Exception ignored) {
        }
    }

    @Auditable(action_type = ActionType.REMOVE_CAR)
    @Loggable
    public Car removeCar(Long id) {
        Car car = carRepository.delete(id);
        return car;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Auditable(action_type = ActionType.EDIT_CAR)
    @Loggable
    public void editCar(Long id, Car car) throws CarNotFound {
        if (!carRepository.editCar(id, car)) {
            throw new CarNotFound();
        }
    }

    @Loggable
    public List<Car> findCarsByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    @Loggable
    public List<Car> findCarsByModel(String model) {
        return carRepository.findByModel(model);
    }

    @Loggable
    public List<Car> findCarsByYear(int year) {
        return carRepository.findByYear(year);
    }

    @Loggable
    public List<Car> findCarsByPriceRange(double minPrice, double maxPrice) {
        return carRepository.findByPriceRange(minPrice, maxPrice);
    }
}