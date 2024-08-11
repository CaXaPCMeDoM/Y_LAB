package com.y_lab.y_lab.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseConnection {
    private Connection connection;
    private Properties properties;

    public DatabaseConnection(String filePath) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException ioException) {
            log.error("Path to the config file is not correct");
            throw new RuntimeException("Failed to load properties file: " + filePath);
        }
    }

    public Connection getConnection() {
        try {
            String username = properties.getProperty("db.username");
            String url = properties.getProperty("db.url");
            String password = properties.getProperty("db.password");

            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException sqlException) {
            log.error("Database was not connect");
            throw new RuntimeException("Failed to establish database connection");
        }
    }
}
