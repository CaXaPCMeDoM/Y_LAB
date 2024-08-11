package com.y_lab.y_lab.starter;

import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.CarService;
import com.y_lab.y_lab.service.OrderService;
import com.y_lab.y_lab.service.UserService;
import com.y_lab.y_lab.service.logger.AuditService;
import com.y_lab.y_lab.starter.manager.AuditManagement;
import com.y_lab.y_lab.starter.manager.CarManagement;
import com.y_lab.y_lab.starter.manager.OrderManagement;
import com.y_lab.y_lab.starter.manager.UserManagement;

/**
 * Класс CarDealerApplication представляет собой главную точку входа для системы управления автодилера.
 * Он координирует управление пользователями, автомобилями, обработку заказов и ведение аудита.
 */
public class CarDealerApplication {
    private final Printer printer;
    private final Input input;

    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final AuditService auditService;

    /**
     * Создает новый экземпляр CarDealerApplication с указанными сервисами и утилитами.
     *
     * @param printer сервис для вывода сообщений пользователю
     * @param input сервис для чтения ввода пользователя
     * @param userService сервис для управления пользователями
     * @param carService сервис для управления автомобилями
     * @param orderService сервис для обработки заказов
     * @param auditService сервис для аудита действий
     */
    public CarDealerApplication(
            Printer printer,
            Input input,
            UserService userService,
            CarService carService,
            OrderService orderService,
            AuditService auditService) {
        this.printer = printer;
        this.input = input;
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.auditService = auditService;
    }

    /**
     * Запускает приложение автодилера, отображает главное меню и обрабатывает выборы пользователя.
     * Этот метод работает в бесконечном цикле, пока пользователь не выберет выход.
     */
    public void start() {
        UserManagement userManagement = new UserManagement(userService, printer, input);
        CarManagement carManagement = new CarManagement(carService, printer, input);
        OrderManagement orderManagement = new OrderManagement(orderService, printer, input);
        AuditManagement auditManagement = new AuditManagement(auditService, printer);

        while (true) {
            showMainMenu();
            int choice = input.readInt();
            switch (choice) {
                case 1:
                    userManagement.registerUser();
                    break;
                case 2:
                    userManagement.loginUser();
                    break;
                case 3:
                    carManagement.manageCars();
                    break;
                case 4:
                    orderManagement.processOrders();
                    break;
                case 5:
                    userManagement.viewUsers();
                    break;
                case 6:
                    auditManagement.viewAuditLog();
                    break;
                case 7:
                    auditManagement.exportAuditLog();
                    break;
                case 0:
                    exit();
                    return;
                default:
                    printer.print("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Отображает варианты главного меню пользователю.
     */
    private void showMainMenu() {
        printer.print("Главное меню:");
        printer.print("1. Зарегистрировать пользователя");
        printer.print("2. Войти в систему");
        printer.print("3. Управление автомобилями");
        printer.print("4. Обработка заказов");
        printer.print("5. Просмотр пользователей");
        printer.print("6. Просмотр журнала аудита");
        printer.print("7. Экспорт журнала аудита");
        printer.print("0. Выход");
    }

    /**
     * Завершает работу приложения, выводя сообщение о завершении.
     */
    private void exit() {
        printer.print("Завершение работы приложения. До свидания!");
    }
}

