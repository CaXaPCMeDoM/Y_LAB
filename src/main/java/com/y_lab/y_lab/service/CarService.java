package com.y_lab.y_lab.service;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.security.UserContext;
import com.y_lab.y_lab.service.logger.AuditService;

import java.util.List;

public class CarService {
    private final CarRepository carRepository;
    private final AuditService auditService;

    public CarService(CarRepository carRepository, AuditService auditService) {
        this.carRepository = carRepository;
        this.auditService = auditService;
    }

    public void addCar(Car car) {
        try {
            carRepository.add(car);

            auditService.log(UserContext.getCurrentUser().getUserId(), ActionType.ADD_CAR);
        } catch (Exception ex) {
            // TODO (log INFO)
        }
    }

    public Car removeCar(Long id) {
        Car car = carRepository.delete(id);

        auditService.log(UserContext.getCurrentUser().getUserId(), ActionType.REMOVE_CAR);
        return car;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void editCar(Long id, Car car) throws CarNotFound {
        if (carRepository.editCar(id, car)) {
            auditService.log(UserContext.getCurrentUser().getUserId(), ActionType.EDIT_CAR);
        } else {
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