package com.y_lab.y_lab.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConfig {
    private static volatile Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DataSourceConfig.class) {
                if (connection == null) {
                    DatabaseConnection databaseConnection = new DatabaseConnection("src\\main\\resources\\application.properties");
                    connection = databaseConnection.getConnection();
                    try {
                        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to set transaction isolation level", e);
                    }
                }
            }
        }
        return connection;
    }
}