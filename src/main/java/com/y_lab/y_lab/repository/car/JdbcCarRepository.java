package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link CarRepository} interface that use JDBC for interactive with a PostgreSQL database.
 * This repository provided basic CRUD operation for the {@link Car} entity, as well as methods for querying {@link Car}
 * by brand, model, year, price range.
 * <p>
 * This class designed to work with PostgreSQL database and assumes 'car' table exists in the specified schema.
 * The schema name is hardcoded as 'entity_schema' in the SQL queries.
 * </p>
 * <p>Transactions are handled manually using JDBC's {@link Connection#setAutoCommit(boolean)} method.
 * In case of an error during a transaction, the changes are rolled back to maintain database consistency.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * Connection connection = DriverManager.getConnection(...);
 * JdbcCarRepository carRepository = new JdbcCarRepository(connection);
 * </pre>
 *
 * <p>This class uses Lombok's {@link Slf4j} annotation for logging SQL errors and transaction failures.</p>
 *
 * @see CarRepository
 * @see Car
 */
@Slf4j
public class JdbcCarRepository implements CarRepository {
    private final Connection connection;

    /**
     * Constructs a new {@code JdbcCarRepository} with the specified JDBC connection.
     *
     * @param connection the JDBC {@link Connection} to be used by this repository
     */
    public JdbcCarRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new {@link Car} to the database.
     *
     * @param car the {@link Car} entity to be added
     * @return the generated ID of the newly added {@link Car}, or {@code null} if the operation fails
     * @throws RuntimeException if the car cannot be added due to a SQL or transaction error
     */
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

    /**
     * Deletes the {@link Car} with the specified ID from the database.
     *
     * @param id the ID of the {@link Car} to be deleted
     * @return the deleted {@link Car} entity, or {@code null} if no car was found with the given ID
     * @throws RuntimeException if the car cannot be deleted due to a SQL or transaction error
     */
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

    /**
     * Retrieves all cars from the database.
     *
     * @return a {@link List} of all {@link Car} entities in the database
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
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

    /**
     * Updates the details of an existing {@link Car} in the database.
     *
     * @param id         the ID of the {@link Car} to be updated
     * @param updatedCar the updated {@link Car} entity
     * @return {@code true} if the car was successfully updated, {@code false} otherwise
     * @throws RuntimeException if the car cannot be updated due to a SQL or transaction error
     */
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

    /**
     * Finds cars in the database by their brand.
     *
     * @param brand the brand of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified brand
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
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

    /**
     * Finds cars in the database by their model.
     *
     * @param model the model of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified model
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
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

    /**
     * Finds cars in the database by their year of manufacture.
     *
     * @param year the year of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified year
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
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

    /**
     * Finds cars in the database within the specified price range.
     *
     * @param minPrice the minimum price of the cars to find
     * @param maxPrice the maximum price of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified price range
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
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

    /**
     * Maps a {@link ResultSet} row to a {@link Car} entity.
     *
     * @param resultSet the {@link ResultSet} to map
     * @return a {@link Car} entity representing the current row in the {@link ResultSet}
     * @throws SQLException if an error occurs while accessing the {@link ResultSet}
     */
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
