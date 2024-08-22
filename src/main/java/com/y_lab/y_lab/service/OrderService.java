package com.y_lab.y_lab.service;

import com.y_lab.y_lab.annotation.Loggable;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.service.logger.AuditService;

import java.sql.Timestamp;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final AuditService auditService;

    public OrderService(OrderRepository orderRepository, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.auditService = auditService;
    }

    @Loggable(action_type = ActionType.CREATE_ORDER)
    public Order createOrder(Long carId, Long customerId) throws OrderForTheCarAlreadyExists {
        if (orderRepository.isOrderExistsForCar(carId)) {
            throw new OrderForTheCarAlreadyExists();
        } else {
            Order order = orderRepository.createOrder(carId, customerId);
            return order;
        }
    }

    public Order searchOrderById(Long orderId) {
        return orderRepository.findOrderById(orderId);
    }

    public void changeStatus(Long orderId, OrderStatus newOrderStatus) {
        orderRepository.changeStatus(orderId, newOrderStatus);
    }

    @Loggable(action_type = ActionType.CANCELED_ORDER)
    public void canceledOrder(Long orderId) {
        orderRepository.changeStatus(orderId, OrderStatus.CANCELLED);
    }

    public List<Order> findOrdersByDate(Timestamp startDate, Timestamp endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }

    // TODO (декоратор, если успею)
    public List<Order> findOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer(customerId);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> findOrdersByCar(Long carId) {
        return orderRepository.findByCar(carId);
    }
}
