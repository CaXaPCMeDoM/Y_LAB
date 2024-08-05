package com.y_lab.y_lab.repository.car.ram;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.repository.car.CarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RamCarRepository implements CarRepository {
    private static List<Car> carList = new ArrayList<>();
    private static Long idCounter = 1L; // TODO (может быть переполнение)

    @Override
    public Long add(Car car) {
        car.setCarId(idCounter);
        carList.add(car);
        return idCounter++;
    }

    @Override
    public Car delete(Long id) {
        for (Car car : carList) {
            if (car.getCarId().equals(id)) {
                carList.remove(car);
                return car;
            }
        }
        return null;
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(carList);
    }

    @Override
    public boolean editCar(Long id, Car updatedCar) {
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getCarId().equals(id)) {
                carList.set(i, updatedCar);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Car> findByBrand(String brand) {
        return carList.stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findByModel(String model) {
        return carList.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findByYear(int year) {
        return carList.stream()
                .filter(car -> car.getYear() == year)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findByPriceRange(double minPrice, double maxPrice) {
        return carList.stream()
                .filter(car -> car.getPrice() >= minPrice && car.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
