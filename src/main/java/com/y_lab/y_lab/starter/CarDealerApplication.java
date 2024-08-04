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

public class CarDealerApplication {
    private final Printer printer;
    private final Input input;

    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final AuditService auditService;

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
                case 0:
                    exit();
                    return;
                default:
                    printer.print("Invalid choice. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        printer.print("Main Menu:");
        printer.print("1. Register User");
        printer.print("2. Login User");
        printer.print("3. Manage Cars");
        printer.print("4. Process Orders");
        printer.print("5. View Users");
        printer.print("6. View Audit Log");
        printer.print("0. Exit");
    }

    private void exit() {
        printer.print("Exiting the application. Goodbye!");
    }
}
