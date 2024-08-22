package com.y_lab.y_lab.config;

import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.out.file.FileExport;
import com.y_lab.y_lab.repository.audit.AuditRepository;
import com.y_lab.y_lab.repository.audit.JdbcAuditRepository;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.repository.car.JdbcCarRepository;
import com.y_lab.y_lab.repository.order.JdbcOrderRepository;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.repository.user.JdbcUserRepository;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.service.CarService;
import com.y_lab.y_lab.service.OrderService;
import com.y_lab.y_lab.service.UserService;
import com.y_lab.y_lab.service.logger.AuditService;

import java.sql.Connection;

public class ServiceContainer {
    private static final String FILENAME_OUTPUT_LOG = "output.txt";
    private static AuditService auditService;
    private static UserService userService;
    private static CarService carService;
    private static OrderService orderService;

    public static AuditService getAuditService() {
        if (auditService == null) {
            initServices();
        }
        return auditService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            initServices();
        }
        return userService;
    }

    public static CarService getCarService() {
        if (carService == null) {
            initServices();
        }
        return carService;
    }

    public static OrderService getOrderService() {
        if (orderService == null) {
            initServices();
        }
        return orderService;
    }

    private static void initServices() {
        Connection connection = DataSourceConfig.getConnection();
        UserRepository userRepository = new JdbcUserRepository(connection);
        CarRepository carRepository = new JdbcCarRepository(connection);
        OrderRepository orderRepository = new JdbcOrderRepository(connection);
        AuditRepository auditRepository = new JdbcAuditRepository(connection);
        Exporter exporter = new FileExport(FILENAME_OUTPUT_LOG);

        auditService = new AuditService(auditRepository, exporter);
        userService = new UserService(userRepository, auditService);
        carService = new CarService(carRepository, auditService);
        orderService = new OrderService(orderRepository, auditService);
    }
}
