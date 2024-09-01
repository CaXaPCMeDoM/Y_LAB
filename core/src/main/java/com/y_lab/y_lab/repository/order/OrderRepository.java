package com.y_lab.y_lab.repository.order;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository {
    Order createOrder(Long carId, Long customerId);

    Order findOrderById(Long orderId);

    void changeStatus(Long orderId, OrderStatus newStatus);

    boolean isOrderExistsForCar(Long carId);

    Order delete(Long orderId);

    Order getFirst();

    List<Order> findByDateRange(Timestamp startDate, Timestamp endDate);

    List<Order> findByCustomer(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCar(Long carId);
}
