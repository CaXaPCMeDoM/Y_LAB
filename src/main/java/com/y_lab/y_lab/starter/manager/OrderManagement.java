package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.OrderService;

import java.sql.Timestamp;
import java.util.List;

public class OrderManagement {
    private final OrderService orderService;
    private final Printer printer;
    private final Input input;

    public OrderManagement(OrderService orderService, Printer printer, Input input) {
        this.orderService = orderService;
        this.printer = printer;
        this.input = input;
    }

    public void processOrders() {
        printer.print("Order Processing Menu:");
        printer.print("1. Create Order");
        printer.print("2. Search Order");
        printer.print("3. Change Order Status");
        printer.print("4. Cancel Order");
        printer.print("5. View Orders by Date");
        printer.print("6. View Orders by Customer");
        printer.print("7. View Orders by Status");
        printer.print("8. View Orders by Car");
        int choice = input.readInt();

        switch (choice) {
            case 1:
                createOrder();
                break;
            case 2:
                searchOrder();
                break;
            case 3:
                changeOrderStatus();
                break;
            case 4:
                cancelOrder();
                break;
            case 5:
                viewOrdersByDate();
                break;
            case 6:
                viewOrdersByCustomer();
                break;
            case 7:
                viewOrdersByStatus();
                break;
            case 8:
                viewOrdersByCar();
                break;
            default:
                printer.print("Invalid choice. Please try again.");
        }
    }

    private void createOrder() {
        printer.print("Enter car ID:");
        Long carId = input.readLong();
        printer.print("Enter customer ID:");
        Long customerId = input.readLong();

        try {
            Order order = orderService.createOrder(carId, customerId);
            printer.print("Order created successfully.");
        } catch (OrderForTheCarAlreadyExists e) {
            printer.print("Order for the car already exists.");
        }
    }

    private void searchOrder() {
        printer.print("Enter order ID:");
        Long orderId = input.readLong();
        Order order = orderService.searchOrderById(orderId);
        if (order != null) {
            printer.print(order.toString());
        } else {
            printer.print("Order not found.");
        }
    }

    private void changeOrderStatus() {
        printer.print("Enter order ID:");
        Long orderId = input.readLong();
        printer.print("Enter new order status (NEW, IN_PROGRESS, COMPLETED, CANCELLED):");
        String status = input.readLine();
        orderService.changeStatus(orderId, OrderStatus.valueOf(status.toUpperCase()));
        printer.print("Order status changed successfully.");
    }

    private void cancelOrder() {
        printer.print("Enter order ID:");
        Long orderId = input.readLong();
        orderService.canceledOrder(orderId);
        printer.print("Order cancelled successfully.");
    }

    private void viewOrdersByDate() {
        printer.print("Enter start date (yyyy-mm-dd hh:mm:ss):");
        String startDate = input.readLine();
        printer.print("Enter end date (yyyy-mm-dd hh:mm:ss):");
        String endDate = input.readLine();
        List<Order> orders = orderService.findOrdersByDate(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    private void viewOrdersByCustomer() {
        printer.print("Enter customer ID:");
        Long customerId = input.readLong();
        List<Order> orders = orderService.findOrdersByCustomer(customerId);
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    private void viewOrdersByStatus() {
        printer.print("Enter order status (NEW, IN_PROGRESS, COMPLETED, CANCELLED):");
        String status = input.readLine();
        List<Order> orders = orderService.findOrdersByStatus(OrderStatus.valueOf(status.toUpperCase()));
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    private void viewOrdersByCar() {
        printer.print("Enter car ID:");
        Long carId = input.readLong();
        List<Order> orders = orderService.findOrdersByCar(carId);
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }
}
