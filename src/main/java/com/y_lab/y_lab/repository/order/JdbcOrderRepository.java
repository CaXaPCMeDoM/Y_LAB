package com.y_lab.y_lab.repository.order;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Slf4j
@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) ->
            new Order(
                    rs.getLong("id"),
                    rs.getLong("car_id"),
                    rs.getLong("user_id"),
                    OrderStatus.valueOf(rs.getString("status")),
                    rs.getTimestamp("order_date"));

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order createOrder(Long carId, Long customerId) {
        String sql = "INSERT INTO entity_schema.order (car_id, user_id, status, order_date) VALUES (?, ?, ?, ?) RETURNING id";
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long orderId = jdbcTemplate.queryForObject(sql, Long.class, carId, customerId, OrderStatus.CREATED.name(), timestamp);
            return new Order(orderId, carId, customerId, OrderStatus.CREATED, timestamp);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findOrderById(Long orderId) {
        String sql = "SELECT * FROM entity_schema.order WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ORDER_ROW_MAPPER, orderId);
        } catch (Exception e) {
            log.error("Error finding order by id", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeStatus(Long orderId, OrderStatus newStatus) {
        String sql = "UPDATE entity_schema.order SET status = ? WHERE id = ?";
        try {
            int affectedRows = jdbcTemplate.update(sql, newStatus.name(), orderId);
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
            }
        } catch (Exception e) {
            log.error("Error updating order status", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order delete(Long orderId) {
        String sql = "DELETE FROM entity_schema.order WHERE id = ? RETURNING *";
        try {
            return jdbcTemplate.queryForObject(sql, ORDER_ROW_MAPPER, orderId);
        } catch (Exception e) {
            log.error("Error deleting order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isOrderExistsForCar(Long carId) {
        String sql = "SELECT 1 FROM entity_schema.order WHERE car_id = ?";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, carId));
        } catch (Exception e) {
            log.error("Error checking if order exists for car", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getFirst() {
        String sql = "SELECT * FROM entity_schema.order ORDER BY id LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, ORDER_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error getting first order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByDateRange(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT * FROM entity_schema.order WHERE order_date BETWEEN ? AND ?";
        try {
            return jdbcTemplate.query(sql, ORDER_ROW_MAPPER, startDate, endDate);
        } catch (Exception e) {
            log.error("Error finding orders by date range", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        String sql = "SELECT * FROM entity_schema.order WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sql, ORDER_ROW_MAPPER, customerId);
        } catch (Exception e) {
            log.error("Error finding orders by customer", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        String sql = "SELECT * FROM entity_schema.order WHERE status = ?";
        try {
            return jdbcTemplate.query(sql, ORDER_ROW_MAPPER, status.name());
        } catch (Exception e) {
            log.error("Error finding orders by status", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByCar(Long carId) {
        String sql = "SELECT * FROM entity_schema.order WHERE car_id = ?";
        try {
            return jdbcTemplate.query(sql, ORDER_ROW_MAPPER, carId);
        } catch (Exception e) {
            log.error("Error finding orders by car", e);
            throw new RuntimeException(e);
        }
    }
}
