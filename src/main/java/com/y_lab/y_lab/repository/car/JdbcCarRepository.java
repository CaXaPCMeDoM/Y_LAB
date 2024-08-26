package com.y_lab.y_lab.repository.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.enums.CarState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
@Repository
public class JdbcCarRepository implements CarRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Car> CAR_ROW_MAPPER = (rs, rowNum) ->
            new Car(
                    rs.getLong("car_id"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("price"),
                    CarState.valueOf(rs.getString("state").toUpperCase()));
    private static final String ADD_ERROR_MESSAGE_EXCEPTION = "Failed to add car to the database";
    private static final String DELETE_ERROR_MESSAGE_EXCEPTION = "Failed to delete car from the database";

    /**
     * Constructs a new {@code JdbcCarRepository} with the specified JDBC connection.
     *
     * @param jdbcTemplate the JDBC {@link JdbcTemplate} to be used by this repository
     */
    public JdbcCarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Adds a new {@link Car} to the database.
     *
     * @param car the {@link Car} entity to be added
     * @return the generated ID of the newly added {@link Car}, or {@code null} if the operation fails
     * @throws RuntimeException if the car cannot be added due to a SQL or transaction error
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long add(Car car) {
        String sql = "INSERT INTO entity_schema.car (brand, model, year, price, state) VALUES (?, ?, ?, ?, ?) RETURNING car_id";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{
                            car.getBrand(),
                            car.getModel(),
                            car.getYear(),
                            car.getPrice(),
                            car.getState().getValue()
                    },
                    Long.class
            );
        } catch (Exception e) {
            log.error(ADD_ERROR_MESSAGE_EXCEPTION, e);
            throw new RuntimeException(ADD_ERROR_MESSAGE_EXCEPTION, e);
        }
    }

    /**
     * Deletes the {@link Car} with the specified ID from the database.
     *
     * @param id the ID of the {@link Car} to be deleted
     * @return the deleted {@link Car} entity, or {@code null} if no car was found with the given ID
     * @throws RuntimeException if the car cannot be deleted due to a SQL or transaction error
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Car delete(Long id) {
        String sql = "DELETE FROM entity_schema.car WHERE car_id = ? RETURNING *";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error(DELETE_ERROR_MESSAGE_EXCEPTION, e);
            throw new RuntimeException(DELETE_ERROR_MESSAGE_EXCEPTION, e);
        }
    }

    /**
     * Retrieves all cars from the database.
     *
     * @return a {@link List} of all {@link Car} entities in the database
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Car> findAll() {
        String sql = "SELECT * FROM entity_schema.car";
        try {
            return jdbcTemplate.query(sql, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Failed to retrieve cars from the database", e);
            throw new RuntimeException("Failed to retrieve cars from the database", e);
        }
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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean editCar(Long id, Car updatedCar) {
        String sql = "UPDATE entity_schema.car SET brand = ?, model = ?, year = ?, price = ?, state = ? WHERE car_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    updatedCar.getBrand(),
                    updatedCar.getModel(),
                    updatedCar.getYear(),
                    updatedCar.getPrice(),
                    updatedCar.getState().getValue(),
                    id
            );
            return rowsAffected > 0;
        } catch (Exception e) {
            log.error("Failed to edit car in the database", e);
            throw new RuntimeException("Failed to edit car in the database", e);
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
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Car> findByBrand(String brand) {
        String sql = "SELECT * FROM entity_schema.car WHERE brand = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{brand}, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Failed to retrieve cars by brand from the database", e);
            throw new RuntimeException("Failed to retrieve cars by brand from the database", e);
        }
    }

    /**
     * Finds cars in the database by their model.
     *
     * @param model the model of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified model
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Car> findByModel(String model) {
        String sql = "SELECT * FROM entity_schema.car WHERE model = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{model}, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Failed to retrieve cars by model from the database", e);
            throw new RuntimeException("Failed to retrieve cars by model from the database", e);
        }
    }

    /**
     * Finds cars in the database by their year of manufacture.
     *
     * @param year the year of the cars to find
     * @return a {@link List} of {@link Car} entities that match the specified year
     * @throws RuntimeException if the cars cannot be retrieved due to a SQL error
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Car> findByYear(int year) {
        String sql = "SELECT * FROM entity_schema.car WHERE year = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{year}, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Failed to retrieve cars by year from the database", e);
            throw new RuntimeException("Failed to retrieve cars by year from the database", e);
        }
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
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Car> findByPriceRange(double minPrice, double maxPrice) {
        String sql = "SELECT * FROM entity_schema.car WHERE price BETWEEN ? AND ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{minPrice, maxPrice}, CAR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Failed to retrieve cars by price range from the database", e);
            throw new RuntimeException("Failed to retrieve cars by price range from the database", e);
        }
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
