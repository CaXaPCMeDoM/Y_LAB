package com.y_lab.y_lab.repository.order;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_ORDER_SQL = "INSERT INTO entity_schema.order (car_id, user_id, status, order_date) VALUES (?, ?, ?, ?) RETURNING id";
    private static final String SELECT_ORDER_BY_ID_SQL = "SELECT * FROM entity_schema.order WHERE id = ?";
    private static final String UPDATE_ORDER_STATUS_SQL = "UPDATE entity_schema.order SET status = ? WHERE id = ?";
    private static final String DELETE_ORDER_SQL = "DELETE FROM entity_schema.order WHERE id = ? RETURNING *";
    private static final String EXISTS_ORDER_FOR_CAR_SQL = "SELECT 1 FROM entity_schema.order WHERE car_id = ?";
    private static final String SELECT_FIRST_ORDER_SQL = "SELECT * FROM entity_schema.order ORDER BY id LIMIT 1";
    private static final String FIND_BY_DATE_RANGE_SQL = "SELECT * FROM entity_schema.order WHERE order_date BETWEEN ? AND ?";
    private static final String FIND_BY_CUSTOMER_SQL = "SELECT * FROM entity_schema.order WHERE user_id = ?";
    private static final String FIND_BY_STATUS_SQL = "SELECT * FROM entity_schema.order WHERE status = ?";
    private static final String FIND_BY_CAR_SQL = "SELECT * FROM entity_schema.order WHERE car_id = ?";

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) ->
            new Order(
                    rs.getLong("id"),
                    rs.getLong("car_id"),
                    rs.getLong("user_id"),
                    OrderStatus.valueOf(rs.getString("status")),
                    rs.getTimestamp("order_date")
            );

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order createOrder(Long carId, Long customerId) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long orderId = jdbcTemplate.queryForObject(INSERT_ORDER_SQL, Long.class, carId, customerId, OrderStatus.CREATED.name(), timestamp);
            return new Order(orderId, carId, customerId, OrderStatus.CREATED, timestamp);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findOrderById(Long orderId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ORDER_BY_ID_SQL, ORDER_ROW_MAPPER, orderId);
        } catch (Exception e) {
            log.error("Error finding order by id", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeStatus(Long orderId, OrderStatus newStatus) {
        try {
            int affectedRows = jdbcTemplate.update(UPDATE_ORDER_STATUS_SQL, newStatus.name(), orderId);
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
        try {
            return jdbcTemplate.queryForObject(DELETE_ORDER_SQL, ORDER_ROW_MAPPER, orderId);
        } catch (Exception e) {
            log.error("Error deleting order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isOrderExistsForCar(Long carId) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXISTS_ORDER_FOR_CAR_SQL, Boolean.class, carId));
        } catch (Exception e) {
            log.error("Error checking if order exists for car", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getFirst() {
        try {
            return jdbcTemplate.queryForObject(SELECT_FIRST_ORDER_SQL, ORDER_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error getting first order", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByDateRange(Timestamp startDate, Timestamp endDate) {
        try {
            return jdbcTemplate.query(FIND_BY_DATE_RANGE_SQL, ORDER_ROW_MAPPER, startDate, endDate);
        } catch (Exception e) {
            log.error("Error finding orders by date range", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        try {
            return jdbcTemplate.query(FIND_BY_CUSTOMER_SQL, ORDER_ROW_MAPPER, customerId);
        } catch (Exception e) {
            log.error("Error finding orders by customer", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        try {
            return jdbcTemplate.query(FIND_BY_STATUS_SQL, ORDER_ROW_MAPPER, status.name());
        } catch (Exception e) {
            log.error("Error finding orders by status", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByCar(Long carId) {
        try {
            return jdbcTemplate.query(FIND_BY_CAR_SQL, ORDER_ROW_MAPPER, carId);
        } catch (Exception e) {
            log.error("Error finding orders by car", e);
            throw new RuntimeException(e);
        }
    }
}
