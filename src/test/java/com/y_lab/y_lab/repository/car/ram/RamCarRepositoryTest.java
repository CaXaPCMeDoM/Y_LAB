package com.y_lab.y_lab.repository.car.ram;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RamCarRepositoryTest {
    private RamCarRepository carRepository;

    @BeforeEach
    public synchronized void setUp() {
        carRepository = new RamCarRepository();
        List<Car> cars = carRepository.findAll();
        for (Car car : cars){
            carRepository.delete(car.getCarId());
        }
    }

    @Test
    void addSuccess() {
        Car car = new Car(null, "Marusya", "B1", 2009, 10000000, CarState.NEW);
        Long idActual = carRepository.add(car);

        Assertions.assertNotNull(idActual);

        List<Car> cars = carRepository.findAll();

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void addError() {
        Assertions.assertThrows(NullPointerException.class, () -> carRepository.add(null));
    }

    @Test
    void delete() {
        Car car = new Car(null, "Marusya", "B1", 2009, 10000000, CarState.NEW);
        Long idActual = carRepository.add(car);

        Car carActual = carRepository.delete(idActual);

        Assertions.assertEquals(car, carActual);

        List<Car> cars = carRepository.findAll();

        Assertions.assertEquals(0, cars.size());
    }

    @Test
    void deleteCarNotFound() {
        Car car = new Car(0L, "Marusya", "B1", 2009, 10000000, CarState.NEW);

        Car carActual = carRepository.delete(car.getCarId() + 1L);

        Assertions.assertNotEquals(car, carActual);
        Assertions.assertNull(carActual);

        List<Car> cars = carRepository.findAll();

        Assertions.assertEquals(0, cars.size());
    }

    @Test
    void deleteCarNotFoundInEmptyList() {
        Assertions.assertNull(carRepository.delete(-1L));
    }

    @Test
    public void findAllCars() {
        Car car1 = new Car(null, "Toyota", "Camry", 2020, 30000, CarState.NEW);
        Car car2 = new Car(null, "Honda", "Civic", 2019, 20000, CarState.USED);
        carRepository.add(car1);
        carRepository.add(car2);

        List<Car> cars = carRepository.findAll();
        assertEquals(2, cars.size());
    }

    @Test
    void editCar() {
        String modelEditedCar = "Makr 2";
        String brandEditedCar = "ToyotaEp";
        Car car = new Car(null, "Marusya", "B2", 2009, 10000000, CarState.NEW);
        Long id = carRepository.add(car);

        Car carAfterUpdate = new Car(id, brandEditedCar, modelEditedCar, 2009, 20100, CarState.USED);
        boolean isUpdate = carRepository.editCar(id, carAfterUpdate);

        Assertions.assertTrue(isUpdate);
        List<Car> editedCars = carRepository.findAll();

        Assertions.assertFalse(editedCars.isEmpty());

        Car editedCar = editedCars.stream().findFirst().get();

        Assertions.assertEquals(modelEditedCar, editedCar.getModel());
        Assertions.assertEquals(brandEditedCar, editedCar.getBrand());
    }

    @Test
    public void editCarNotFound() {
        Car updatedCar = new Car(1L, "Toyota", "Makr 2", 2021, 25000, CarState.USED);
        boolean result = carRepository.editCar(1L, updatedCar);

        assertFalse(result);
    }

    @Test
    void findByBrand() {
        Car carFirst = new Car(1L, "Toyota", "Makr 2", 2021, 25000, CarState.USED);
        Car carSecond = new Car(null, "Marusya", "B2", 2009, 10000000, CarState.NEW);
        carRepository.add(carFirst);
        carRepository.add(carSecond);

        List<Car> toyotaCars = carRepository.findByBrand("Toyota");
        assertEquals(1, toyotaCars.size());
        assertEquals("Makr 2", toyotaCars.get(0).getModel());
    }

    @Test
    void findByModel() {
        Car carFirst = new Car(1L, "Toyota", "Makr 2", 2021, 25000, CarState.USED);
        Car carSecond = new Car(null, "Marusya", "B2", 2009, 10000000, CarState.NEW);
        carRepository.add(carFirst);
        carRepository.add(carSecond);

        List<Car> civicCars = carRepository.findByModel("Makr 2");
        assertEquals(1, civicCars.size());
        assertEquals("Toyota", civicCars.get(0).getBrand());
    }

    @Test
    void findByYear() {
        Car carFirst = new Car(1L, "Toyota", "Makr 2", 2020, 25000, CarState.USED);
        Car carSecond = new Car(null, "Marusya", "B2", 2009, 10000000, CarState.NEW);
        carRepository.add(carFirst);
        carRepository.add(carSecond);

        List<Car> cars2020 = carRepository.findByYear(2020);
        assertEquals(1, cars2020.size());
        assertEquals("Makr 2", cars2020.get(0).getModel());
    }

    @Test
    void findByPriceRange() {
        Car carFirst = new Car(1L, "Toyota", "Makr 2", 2021, 25000, CarState.USED);
        Car carSecond = new Car(null, "Marusya", "B2", 2009, 10000000, CarState.NEW);
        carRepository.add(carFirst);
        carRepository.add(carSecond);

        List<Car> carsInRange = carRepository.findByPriceRange(25000, 35000);
        assertEquals(1, carsInRange.size());
        assertEquals("Makr 2", carsInRange.get(0).getModel());
    }
}