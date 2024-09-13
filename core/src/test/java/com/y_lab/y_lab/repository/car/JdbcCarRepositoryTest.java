package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.IntegrationEnvironment;
import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcCarRepositoryTest extends IntegrationEnvironment {
    private JdbcCarRepository carRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        carRepository = new JdbcCarRepository(jdbcTemplate);
    }


    @Test
    public void addCar() {
        Car car = new Car(null, "Toyota", "Corolla", 2020, 20000.0, CarState.NEW);
        Long carId = carRepository.add(car);
        assertNotNull(carId);

        List<Car> cars = carRepository.findAll();
        assertEquals(cars.get(cars.size() - 1).getCarId(), carId);
    }

    @Test
    public void deleteCar() {
        Car car = new Car(null, "Honda", "Civic", 2019, 18000.0, CarState.USED);
        Long carId = carRepository.add(car);
        List<Car> carsBefore = carRepository.findAll();

        Car deletedCar = carRepository.delete(carId);
        assertNotNull(deletedCar);
        assertEquals(carId, deletedCar.getCarId());

        List<Car> carsAfter = carRepository.findAll();
        assertEquals(carsBefore.size(), carsAfter.size() + 1);
    }

    @Test
    public void findByBrand() {
        Car carFirst = new Car(null, "Toyota", "Camry", 2021, 25000.0, CarState.NEW);
        Car carSecond = new Car(null, "Toyota", "Corolla", 2020, 20000.0, CarState.NEW);
        Long carFirstId = carRepository.add(carFirst);
        Long carSecondId = carRepository.add(carSecond);

        List<Car> toyotaCars = carRepository.findByBrand("Toyota");
        boolean flagFirst = false;
        boolean flagSecond = false;
        for (Car car : toyotaCars) {
            if (carFirstId.equals(car.getCarId())) {
                flagFirst = true;
            }
            if (carSecondId.equals(car.getCarId())) {
                flagSecond = true;
            }
        }
        assertTrue(flagFirst);
        assertTrue(flagSecond);
    }
}