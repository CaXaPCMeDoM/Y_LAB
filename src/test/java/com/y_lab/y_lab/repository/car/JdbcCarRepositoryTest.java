package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class JdbcCarRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("db")
            .withUsername("caxap")
            .withPassword("1234");

    private static Connection connection;
    private static JdbcCarRepository carRepository;

    @BeforeAll
    public static void setUp() throws Exception {
        // Устанавливаем соединение с БД
        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        carRepository = new JdbcCarRepository(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testAddCar() {
        Car car = new Car(null, "Toyota", "Corolla", 2020, 20000.0, CarState.NEW);
        Long carId = carRepository.add(car);
        assertNotNull(carId);

        List<Car> cars = carRepository.findAll();
        assertEquals(1, cars.size());
        assertEquals("Toyota", cars.get(0).getBrand());
    }

    @Test
    public void testDeleteCar() {
        Car car = new Car(null, "Honda", "Civic", 2019, 18000.0, CarState.USED);
        Long carId = carRepository.add(car);

        Car deletedCar = carRepository.delete(carId);
        assertNotNull(deletedCar);
        assertEquals(carId, deletedCar.getCarId());

        List<Car> cars = carRepository.findAll();
        assertTrue(cars.isEmpty());
    }

    @Test
    public void testFindByBrand() {
        Car car1 = new Car(null, "Toyota", "Camry", 2021, 25000.0, CarState.NEW);
        Car car2 = new Car(null, "Toyota", "Corolla", 2020, 20000.0, CarState.NEW);
        carRepository.add(car1);
        carRepository.add(car2);

        List<Car> toyotaCars = carRepository.findByBrand("Toyota");
        assertEquals(2, toyotaCars.size());
    }
}