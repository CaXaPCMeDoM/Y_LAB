package com.y_lab.y_lab.service;

import org.spring.boot.starter.my.annotation.Auditable;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.service.logger.AuditService;
import entity.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import y_lab.annotaion.Loggable;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuditService auditService;

    @Auditable(action_type = ActionType.CREATE_ORDER)
    @Loggable
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

    @Auditable(action_type = ActionType.CANCELED_ORDER)
    @Loggable
    public void canceledOrder(Long orderId) {
        orderRepository.changeStatus(orderId, OrderStatus.CANCELLED);
    }

    @Loggable
    public List<Order> findOrdersByDate(Timestamp startDate, Timestamp endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }

    @Loggable
    public List<Order> findOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer(customerId);
    }

    @Loggable
    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Loggable
    public List<Order> findOrdersByCar(Long carId) {
        return orderRepository.findByCar(carId);
    }
}
