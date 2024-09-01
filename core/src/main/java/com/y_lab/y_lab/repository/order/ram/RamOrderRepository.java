package com.y_lab.y_lab.repository.order.ram;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.repository.order.OrderRepository;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Deprecated
public class RamOrderRepository implements OrderRepository {
    @Getter
    private static Queue<Order> orderQueue = new LinkedList<>();
    @Setter
    @Getter
    private static Long orderId = 1L;

    @Override
    public Order createOrder(Long carId, Long customerId) {
        Order order = new Order(orderId++, carId, customerId, OrderStatus.CREATED, new Timestamp(System.currentTimeMillis()));
        orderQueue.add(order);
        return order;
    }

    @Override
    public Order findOrderById(Long orderId) {
        for (Order order : orderQueue) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    @Override
    public void changeStatus(Long orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
    }

    @Override
    public Order delete(Long orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            orderQueue.remove(order);
            return order;
        } else {
            throw new IllegalArgumentException("Заказ с ID " + orderId + " не найден.");
        }
    }

    /**
     * @param carId id из сущности Car
     * @return возращает true, если заказ на машину уже существует;
     * false - если еще нет заказа на конкретный автомобиль
     */
    @Override
    public boolean isOrderExistsForCar(Long carId) {
        for (Order order : orderQueue) {
            if (order.getId().equals(carId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Order getFirst() {
        return orderQueue.peek();
    }

    @Override
    public List<Order> findByDateRange(Timestamp startDate, Timestamp endDate) {
        return orderQueue.stream()
                .filter(order -> order.getOrderDate().after(startDate) && order.getOrderDate().before(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        return orderQueue.stream()
                .filter(order -> order.getUserId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderQueue.stream()
                .filter(order -> order.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCar(Long carId) {
        return orderQueue.stream()
                .filter(order -> order.getCarId().equals(carId))
                .collect(Collectors.toList());
    }
}
