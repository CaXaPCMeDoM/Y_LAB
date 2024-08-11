package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.CarService;

import java.util.List;

/**
 * Класс CarManagement предоставляет функциональность для управления автомобилями.
 */
public class CarManagement {
    private final CarService carService;
    private final Printer printer;
    private final Input input;

    /**
     * Создает новый экземпляр CarManagement с указанными сервисом автомобилей, утилитой вывода и утилитой ввода.
     *
     * @param carService сервис для работы с автомобилями
     * @param printer утилита для вывода сообщений пользователю
     * @param input утилита для получения ввода от пользователя
     */
    public CarManagement(CarService carService, Printer printer, Input input) {
        this.carService = carService;
        this.printer = printer;
        this.input = input;
    }

    /**
     * Отображает меню управления автомобилями и обрабатывает выбор пользователя.
     */
    public void manageCars() {
        printer.print("Car Management Menu:");
        printer.print("1. Add Car");
        printer.print("2. Edit Car");
        printer.print("3. Remove Car");
        printer.print("4. View All Cars");
        int choice = input.readInt();

        switch (choice) {
            case 1:
                addCar();
                break;
            case 2:
                editCar();
                break;
            case 3:
                removeCar();
                break;
            case 4:
                viewAllCars();
                break;
            default:
                printer.print("Invalid choice. Please try again.");
        }
    }

    /**
     * Добавляет новый автомобиль на основании данных, введенных пользователем.
     */
    private void addCar() {
        printer.print("Enter brand:");
        String brand = input.readLine();
        printer.print("Enter model:");
        String model = input.readLine();
        printer.print("Enter year:");
        int year = input.readInt();
        printer.print("Enter price:");
        double price = input.readDouble();
        printer.print("Enter condition (NEW/USED):");
        String condition = input.readLine();

        CarState carState;
        try {
            carState = CarState.valueOf(condition.toUpperCase());
        } catch (IllegalArgumentException e) {
            printer.print("Invalid car state. Defaulting to NEW.");
            carState = CarState.NEW;
        }

        Car car = new Car(null, brand, model, year, price, carState);
        carService.addCar(car);
        printer.print("Car added successfully.");
    }

    /**
     * Редактирует существующий автомобиль на основании данных, введенных пользователем.
     */
    private void editCar() {
        printer.print("Enter car ID to edit:");
        Long carId = input.readLong();
        printer.print("Enter new brand:");
        String brand = input.readLine();
        printer.print("Enter new model:");
        String model = input.readLine();
        printer.print("Enter new year:");
        int year = input.readInt();
        printer.print("Enter new price:");
        double price = input.readDouble();
        printer.print("Enter new condition (NEW/USED):");
        String condition = input.readLine();

        CarState carState;
        try {
            carState = CarState.valueOf(condition.toUpperCase());
        } catch (IllegalArgumentException e) {
            printer.print("Invalid car state. Defaulting to NEW.");
            carState = CarState.NEW;
        }

        Car car = new Car(null, brand, model, year, price, carState);
        try {
            carService.editCar(carId, car);
            printer.print("Car edited successfully.");
        } catch (CarNotFound e) {
            printer.print("Car not found.");
        }
    }

    /**
     * Удаляет существующий автомобиль на основании идентификатора, введенного пользователем.
     */
    private void removeCar() {
        printer.print("Enter car ID to remove:");
        Long carId = input.readLong();
        Car car = carService.removeCar(carId);
        if (car != null) {
            printer.print("Car removed successfully.");
        } else {
            printer.print("Car not found.");
        }
    }

    /**
     * Отображает все автомобили.
     */
    private void viewAllCars() {
        List<Car> cars = carService.getAllCars();
        for (Car car : cars) {
            printer.print(car.toString());
        }
    }
}
