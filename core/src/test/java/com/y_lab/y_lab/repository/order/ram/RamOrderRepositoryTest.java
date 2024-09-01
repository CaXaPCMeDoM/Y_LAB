package com.y_lab.y_lab.repository.order.ram;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RamOrderRepositoryTest {
    private RamOrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository = new RamOrderRepository();
        RamOrderRepository.getOrderQueue().clear();
        RamOrderRepository.setOrderId(1L);
    }

    @Test
    public void testCreateOrder() {
        Order order = orderRepository.createOrder(1L, 1L);
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    public void testFindOrderById() {
        orderRepository.createOrder(1L, 1L);
        Order order = orderRepository.findOrderById(1L);
        assertNotNull(order);
        assertEquals(1L, order.getId());
    }

    @Test
    public void testChangeStatus() {
        orderRepository.createOrder(1L, 1L);
        orderRepository.changeStatus(1L, OrderStatus.COMPLETED);
        Order order = orderRepository.findOrderById(1L);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testDeleteOrder() {
        orderRepository.createOrder(1L, 1L);
        Order order = orderRepository.delete(1L);
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertNull(orderRepository.findOrderById(1L));
    }

    @Test
    public void testIsOrderExistsForCar() {
        orderRepository.createOrder(1L, 1L);
        assertTrue(orderRepository.isOrderExistsForCar(1L));
        assertFalse(orderRepository.isOrderExistsForCar(2L));
    }

    @Test
    public void testGetFirst() {
        Order order1 = orderRepository.createOrder(1L, 1L);
        assertEquals(order1, orderRepository.getFirst());
    }

    @Test
    public void testFindByDateRange() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        orderRepository.createOrder(1L, 1L);
        List<Order> orders = orderRepository.findByDateRange(new Timestamp(now.getTime() - 1000), new Timestamp(now.getTime() + 1000));
        assertEquals(1, orders.size());
    }

    @Test
    public void testFindByCustomer() {
        orderRepository.createOrder(1L, 1L);
        orderRepository.createOrder(2L, 2L);
        List<Order> orders = orderRepository.findByCustomer(1L);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getUserId());
    }

    @Test
    public void testFindByStatus() {
        orderRepository.createOrder(1L, 1L);
        orderRepository.changeStatus(1L, OrderStatus.COMPLETED);
        List<Order> orders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        assertEquals(1, orders.size());
        assertEquals(OrderStatus.COMPLETED, orders.get(0).getStatus());
    }

    @Test
    public void testFindByCar() {
        orderRepository.createOrder(1L, 1L);
        orderRepository.createOrder(2L, 2L);
        List<Order> orders = orderRepository.findByCar(1L);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getCarId());
    }
}