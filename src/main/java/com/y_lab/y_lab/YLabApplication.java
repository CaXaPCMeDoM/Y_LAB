package com.y_lab.y_lab;

import com.y_lab.y_lab.config.DataSourceConfig;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.in.console.ConsoleInput;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.out.console.PrintToConsole;
import com.y_lab.y_lab.starter.CarDealerApplication;

import java.sql.Connection;
import java.sql.SQLException;

public class YLabApplication {

    public static void main(String[] args) {
        Printer printer = new PrintToConsole();
        Input input = new ConsoleInput();

        CarDealerApplication app = getCarDealerApplication(DataSourceConfig.getConnection(), printer, input);
        app.start();

        try {
            DataSourceConfig.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static CarDealerApplication getCarDealerApplication(Connection connection, Printer printer, Input input) {
        return new CarDealerApplication(
                printer,
                input,
                ServiceContainer.getUserService(),
                ServiceContainer.getCarService(),
                ServiceContainer.getOrderService(),
                ServiceContainer.getAuditService());
    }
}
