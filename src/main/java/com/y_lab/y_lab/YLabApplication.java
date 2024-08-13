package com.y_lab.y_lab;

import com.y_lab.y_lab.config.DatabaseConnection;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.in.console.ConsoleInput;
import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.out.console.PrintToConsole;
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
import com.y_lab.y_lab.starter.CarDealerApplication;

import java.sql.Connection;
import java.sql.SQLException;

public class YLabApplication {
    private final static String FILENAME_OUTPUT_LOG = "output.txt";
    private final static String CONFIG_FILE_PATH = "src\\main\\resources\\application.properties";

    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection(CONFIG_FILE_PATH);
        Connection connection = databaseConnection.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        Printer printer = new PrintToConsole();
        Input input = new ConsoleInput();

        CarDealerApplication app = getCarDealerApplication(connection, printer, input);
        app.start();

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static CarDealerApplication getCarDealerApplication(Connection connection, Printer printer, Input input) {
        UserRepository userRepository = new JdbcUserRepository(connection);
        CarRepository carRepository = new JdbcCarRepository(connection);
        OrderRepository orderRepository = new JdbcOrderRepository(connection);
        AuditRepository auditRepository = new JdbcAuditRepository(connection);
        Exporter exporter = new FileExport(FILENAME_OUTPUT_LOG);

        AuditService auditService = new AuditService(auditRepository, exporter);
        UserService userService = new UserService(userRepository, auditService);
        CarService carService = new CarService(carRepository, auditService);
        OrderService orderService = new OrderService(orderRepository, auditService);

        return new CarDealerApplication(printer, input, userService, carService, orderService, auditService);
    }
}
