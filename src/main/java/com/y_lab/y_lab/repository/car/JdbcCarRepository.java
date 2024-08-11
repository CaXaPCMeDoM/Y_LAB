package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcCarRepository implements CarRepository {
    private final Connection connection;

    public JdbcCarRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Long add(Car car) {
        String sql = "INSERT INTO entity_schema.car (brand, model, year, price, state) VALUES (?, ?, ?, ?, ?) RETURNING car_id";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, car.getBrand());
                statement.setString(2, car.getModel());
                statement.setInt(3, car.getYear());
                statement.setDouble(4, car.getPrice());
                statement.setString(5, car.getState().getValue());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Long carId = resultSet.getLong("car_id");
                    connection.commit();
                    return carId;
                }
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error executing SQL query", e);
                throw new RuntimeException("Failed to add car to the database", e);
            }
        } catch (SQLException e) {
            log.error("Transaction error", e);
            throw new RuntimeException("Failed to handle transaction", e);
        }
        return null;
    }

    @Override
    public Car delete(Long id) {
        String sql = "DELETE FROM entity_schema.car WHERE car_id = ? RETURNING *";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Car car = mapRowToCar(resultSet);
                    connection.commit();
                    return car;
                }
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error executing SQL query", e);
                throw new RuntimeException("Failed to delete car from the database", e);
            }
        } catch (SQLException e) {
            log.error("Transaction error", e);
            throw new RuntimeException("Failed to handle transaction", e);
        }
        return null;
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.car";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException("Failed to retrieve cars from the database", e);
        }
        return cars;
    }

    @Override
    public boolean editCar(Long id, Car updatedCar) {
        String sql = "UPDATE entity_schema.car SET brand = ?, model = ?, year = ?, price = ?, state = ? WHERE car_id = ?";
        try {
            connection.setAutoCommit(false); // Начало транзакции
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, updatedCar.getBrand());
                statement.setString(2, updatedCar.getModel());
                statement.setInt(3, updatedCar.getYear());
                statement.setDouble(4, updatedCar.getPrice());
                statement.setString(5, updatedCar.getState().getValue());
                statement.setLong(6, id);
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error executing SQL query", e);
                throw new RuntimeException("Failed to edit car in the database", e);
            }
        } catch (SQLException e) {
            log.error("Transaction error", e);
            throw new RuntimeException("Failed to handle transaction", e);
        }
    }

    @Override
    public List<Car> findByBrand(String brand) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.car WHERE brand = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, brand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException("Failed to retrieve cars by brand from the database", e);
        }
        return cars;
    }

    @Override
    public List<Car> findByModel(String model) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.car WHERE model = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException("Failed to retrieve cars by model from the database", e);
        }
        return cars;
    }

    @Override
    public List<Car> findByYear(int year) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.car WHERE year = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, year);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException("Failed to retrieve cars by year from the database", e);
        }
        return cars;
    }

    @Override
    public List<Car> findByPriceRange(double minPrice, double maxPrice) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.car WHERE price BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException("Failed to retrieve cars by price range from the database", e);
        }
        return cars;
    }

    private Car mapRowToCar(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getLong("car_id"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getInt("year"),
                resultSet.getDouble("price"),
                CarState.valueOf(resultSet.getString("state").toUpperCase())
        );
    }
}
