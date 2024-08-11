package com.y_lab.y_lab.repository.order;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcOrderRepository implements OrderRepository {
    private final Connection connection;

    public JdbcOrderRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Order createOrder(Long carId, Long customerId) {
        String sql = "INSERT INTO entity_schema.order (car_id, user_id, status, order_date) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            statement.setLong(1, carId);
            statement.setLong(2, customerId);
            statement.setString(3, OrderStatus.CREATED.toString());
            statement.setTimestamp(4, timestamp);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Order(
                        resultSet.getLong("id"),
                        carId,
                        customerId,
                        OrderStatus.CREATED,
                        timestamp
                );
            }
        } catch (SQLException e) {
            log.error("Error created order SQL query");
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Order findOrderById(Long orderId) {
        String sql = "SELECT * FROM entity_schema.order WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToOrder(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error find order by id SQL query");
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void changeStatus(Long orderId, OrderStatus newStatus) {
        String sql = "UPDATE entity_schema.order SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus.name());
            statement.setLong(2, orderId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order delete(Long orderId) {
        String sql = "DELETE FROM entity_schema.order WHERE id = ? RETURNING *";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToOrder(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean isOrderExistsForCar(Long carId) {
        String sql = "SELECT 1 FROM entity_schema.order WHERE car_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, carId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getFirst() {
        String sql = "SELECT * FROM entity_schema.order ORDER BY id LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToOrder(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Order> findByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.order WHERE order_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, startDate);
            statement.setTimestamp(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.order WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.order WHERE status = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> findByCar(Long carId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.order WHERE car_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, carId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return orders;
    }

    private Order mapRowToOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getLong("id"),
                resultSet.getLong("car_id"),
                resultSet.getLong("user_id"),
                OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("order_date")
        );
    }
}
