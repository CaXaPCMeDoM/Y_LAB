package com.y_lab.y_lab;

import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.in.console.ConsoleInput;
import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.out.console.PrintToConsole;
import com.y_lab.y_lab.out.file.FileExport;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.repository.car.ram.RamCarRepository;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.repository.order.ram.RamOrderRepository;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.repository.user.ram.RamUserRepository;
import com.y_lab.y_lab.service.CarService;
import com.y_lab.y_lab.service.OrderService;
import com.y_lab.y_lab.service.UserService;
import com.y_lab.y_lab.service.logger.AuditService;
import com.y_lab.y_lab.starter.CarDealerApplication;

public class YLabApplication {
    private final static String FILENAME_OUTPUT_LOG = "output.txt";
    public static void main(String[] args) {
        Printer printer = new PrintToConsole();
        Input input = new ConsoleInput();

        UserRepository userRepository = new RamUserRepository();
        CarRepository carRepository = new RamCarRepository();
        OrderRepository orderRepository = new RamOrderRepository();
        Exporter exporter = new FileExport(FILENAME_OUTPUT_LOG);

        AuditService auditService = new AuditService(exporter);
        UserService userService = new UserService(userRepository, auditService);
        CarService carService = new CarService(carRepository, auditService);
        OrderService orderService = new OrderService(orderRepository, auditService);

        CarDealerApplication app = new CarDealerApplication(printer, input, userService, carService, orderService, auditService);
        app.start();

    }
}
