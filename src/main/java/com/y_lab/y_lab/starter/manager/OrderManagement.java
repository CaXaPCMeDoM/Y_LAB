package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.OrderService;

import java.sql.Timestamp;
import java.util.List;

/**
 * Класс OrderManagement предоставляет функциональность для управления заказами,
 * включая создание, поиск, изменение статуса, отмену и просмотр заказов по различным критериям.
 */
public class OrderManagement {
    private final OrderService orderService;
    private final Printer printer;
    private final Input input;

    /**
     * Создает новый экземпляр OrderManagement с указанными сервисами и утилитами.
     *
     * @param orderService сервис для управления заказами
     * @param printer сервис для вывода сообщений пользователю
     * @param input сервис для чтения ввода пользователя
     */
    public OrderManagement(OrderService orderService, Printer printer, Input input) {
        this.orderService = orderService;
        this.printer = printer;
        this.input = input;
    }

    /**
     * Обрабатывает заказы, предоставляя пользователю меню с различными действиями,
     * такими как создание, поиск, изменение статуса, отмена и просмотр заказов.
     */
    public void processOrders() {
        printer.print("Меню обработки заказов:");
        printer.print("1. Создать заказ");
        printer.print("2. Найти заказ");
        printer.print("3. Изменить статус заказа");
        printer.print("4. Отменить заказ");
        printer.print("5. Просмотреть заказы по дате");
        printer.print("6. Просмотреть заказы по клиенту");
        printer.print("7. Просмотреть заказы по статусу");
        printer.print("8. Просмотреть заказы по автомобилю");
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
                printer.print("Неверный выбор. Пожалуйста, попробуйте снова.");
        }
    }

    /**
     * Создает новый заказ, запрашивая у пользователя идентификаторы автомобиля и клиента.
     * Выводит соответствующее сообщение в случае успешного создания или ошибки.
     */
    private void createOrder() {
        printer.print("Введите идентификатор автомобиля:");
        Long carId = input.readLong();
        printer.print("Введите идентификатор клиента:");
        Long customerId = input.readLong();

        try {
            orderService.createOrder(carId, customerId);
            printer.print("Заказ успешно создан.");
        } catch (OrderForTheCarAlreadyExists e) {
            printer.print("Заказ на данный автомобиль уже существует.");
        }
    }

    /**
     * Ищет заказ по его идентификатору и выводит его информацию.
     * Выводит сообщение, если заказ не найден.
     */
    private void searchOrder() {
        printer.print("Введите идентификатор заказа:");
        Long orderId = input.readLong();
        Order order = orderService.searchOrderById(orderId);
        if (order != null) {
            printer.print(order.toString());
        } else {
            printer.print("Заказ не найден.");
        }
    }

    /**
     * Изменяет статус заказа, запрашивая у пользователя идентификатор заказа и новый статус.
     * Выводит сообщение о результате операции.
     */
    private void changeOrderStatus() {
        printer.print("Введите идентификатор заказа:");
        Long orderId = input.readLong();
        printer.print("Введите новый статус заказа (NEW, IN_PROGRESS, COMPLETED, CANCELLED):");
        String status = input.readLine();
        orderService.changeStatus(orderId, OrderStatus.valueOf(status.toUpperCase()));
        printer.print("Статус заказа успешно изменен.");
    }

    /**
     * Отменяет заказ, запрашивая у пользователя его идентификатор.
     * Выводит сообщение о результате операции.
     */
    private void cancelOrder() {
        printer.print("Введите идентификатор заказа:");
        Long orderId = input.readLong();
        orderService.canceledOrder(orderId);
        printer.print("Заказ успешно отменен.");
    }

    /**
     * Отображает заказы в указанном диапазоне дат.
     * Запрашивает у пользователя начальную и конечную даты.
     */
    private void viewOrdersByDate() {
        printer.print("Введите начальную дату (yyyy-mm-dd hh:mm:ss):");
        String startDate = input.readLine();
        printer.print("Введите конечную дату (yyyy-mm-dd hh:mm:ss):");
        String endDate = input.readLine();
        List<Order> orders = orderService.findOrdersByDate(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    /**
     * Отображает заказы, выполненные указанным клиентом.
     * Запрашивает у пользователя идентификатор клиента.
     */
    private void viewOrdersByCustomer() {
        printer.print("Введите идентификатор клиента:");
        Long customerId = input.readLong();
        List<Order> orders = orderService.findOrdersByCustomer(customerId);
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    /**
     * Отображает заказы с указанным статусом.
     * Запрашивает у пользователя статус заказа.
     */
    private void viewOrdersByStatus() {
        printer.print("Введите статус заказа (NEW, IN_PROGRESS, COMPLETED, CANCELLED):");
        String status = input.readLine();
        List<Order> orders = orderService.findOrdersByStatus(OrderStatus.valueOf(status.toUpperCase()));
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }

    /**
     * Отображает заказы на указанный автомобиль.
     * Запрашивает у пользователя идентификатор автомобиля.
     */
    private void viewOrdersByCar() {
        printer.print("Введите идентификатор автомобиля:");
        Long carId = input.readLong();
        List<Order> orders = orderService.findOrdersByCar(carId);
        for (Order order : orders) {
            printer.print(order.toString());
        }
    }
}

