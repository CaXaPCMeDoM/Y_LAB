package com.y_lab.y_lab.service;

import com.y_lab.y_lab.annotation.Loggable;
import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.service.logger.AuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Loggable(action_type = ActionType.ADD_CAR)
    public void addCar(Car car) {
        try {
            carRepository.add(car);
        } catch (Exception ignored) {
        }
    }

    @Loggable(action_type = ActionType.REMOVE_CAR)
    public Car removeCar(Long id) {
        Car car = carRepository.delete(id);
        return car;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Loggable(action_type = ActionType.EDIT_CAR)
    public void editCar(Long id, Car car) throws CarNotFound {
        if (!carRepository.editCar(id, car)) {
            throw new CarNotFound();
        }
    }

    // TODO (декоратор, если успею)
    public List<Car> findCarsByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    public List<Car> findCarsByModel(String model) {
        return carRepository.findByModel(model);
    }

    public List<Car> findCarsByYear(int year) {
        return carRepository.findByYear(year);
    }

    public List<Car> findCarsByPriceRange(double minPrice, double maxPrice) {
        return carRepository.findByPriceRange(minPrice, maxPrice);
    }
}